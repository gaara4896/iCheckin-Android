package my.com.icheckin.icheckin_android.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.pawegio.kandroid.longToast
import com.pawegio.kandroid.visible
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Izone
import my.com.icheckin.icheckin_android.model.entity.Credential
import my.com.icheckin.icheckin_android.utils.storage.AppDatabase
import my.com.icheckin.icheckin_android.utils.view.CustomProgressDialog
import org.json.JSONObject

class AddAccountActivity : AppCompatActivity() {

    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var studentInfo: Map<String, String>

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        progressDialog = CustomProgressDialog(this, "Registering...")

        imageButton_Close.setOnClickListener {
            finish()
        }

        webView_Izone.visible = false

        webView_Izone.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view!!.visible = false
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (url == "https://izone.sunway.edu.my/login" && !::studentInfo.isInitialized) {
                    view!!.evaluateJavascript("(function () { " +
                            "var header = document.getElementById(\"header\");" +
                            "header.parentNode.removeChild(header);" +
                            "var footerLinks = document.getElementById(\"footer-links\");" +
                            "footerLinks.parentNode.removeChild(footerLinks);" +
                            "var footerCopyright = document.getElementById(\"footer-copyright\");" +
                            "footerCopyright.parentNode.removeChild(footerCopyright);" +
                            "})();") { _ -> }
                    view.evaluateJavascript("(function () {" +
                            "document.getElementById(\"test1\").style.backgroundImage = \"url('https://i.imgur.com/J3aIvHw.png')\";" +
                            "var formGroup = document.getElementsByClassName(\"form-group\");" +
                            "formGroup[2].parentNode.removeChild(formGroup[2]);" +
                            "formGroup[2].parentNode.removeChild(formGroup[2]);" +
                            "})();") { _ -> }
                    view.visible = true
                } else {

                    when (url) {
                        "https://izone.sunway.edu.my/" -> {
                            progressDialog.show()
                            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                            view!!.loadUrl("https://izone.sunway.edu.my/student")
                        }
                        "https://izone.sunway.edu.my/student" -> {
                            view!!.evaluateJavascript("(function () {" +
                                    "var student = new Object;" +
                                    "student.name = document.getElementsByClassName(\"col-xs-12\")[2].childNodes[3].innerText;" +
                                    "student.username = document.getElementsByTagName(\"td\")[1].innerText;" +
                                    "return student;" +
                                    "})();"
                            ) { value ->
                                val jsonObj = JSONObject(value)
                                studentInfo = mapOf(
                                        "username" to jsonObj.getString("username"),
                                        "name" to jsonObj.getString("name")
                                )
                            }
                            view.loadUrl("https://izone.sunway.edu.my/icheckin/ReGenerateCode")
                        }
                        "https://izone.sunway.edu.my/icheckin/ReGenerateCode" -> {
                            view!!.evaluateJavascript("(function () {" +
                                    "return document.getElementById(\"code\").childNodes[0].innerText" +
                                    "})();") { value -> registerAccount(value.replace("\"", "")) }
                            view.loadUrl("https://izone.sunway.edu.my/logout")
                        }
                    }
                }
            }
        }

        webView_Izone.canGoBackOrForward(10000)
        webView_Izone.settings.javaScriptEnabled = true

        webView_Izone.loadUrl("https://izone.sunway.edu.my/login")

    }

    private fun registerAccount(otp: String) {

        async {
            val db = AppDatabase.getDatabase(applicationContext)
            if (db.credentialDao().query(studentInfo["username"]!!) != null) {
                launch(UI) { longToast("${studentInfo["username"]} already exists") }
            } else {
                val result = Izone.register(studentInfo["username"]!!, otp)
                when (result["status"] as Int) {
                    0 -> {
                        db.credentialDao().insert(
                                Credential(studentInfo["username"]!!, result["device_id"] as String, studentInfo["name"])
                        )
                        launch(UI) { longToast("Success") }
                    }
                    1 -> launch(UI) { longToast("Please try again later") }
                    2 -> launch(UI) { longToast("No internet connection") }
                    3 -> launch(UI) { longToast("Not connected to uni wifi") }
                }
            }
            launch(UI) {
                progressDialog.hide()
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                finish()
            }
        }
    }
}
