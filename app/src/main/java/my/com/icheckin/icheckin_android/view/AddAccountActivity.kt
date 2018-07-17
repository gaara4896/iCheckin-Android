package my.com.icheckin.icheckin_android.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.pawegio.kandroid.longToast
import com.pawegio.kandroid.textWatcher
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Izone
import my.com.icheckin.icheckin_android.model.entity.Credential
import my.com.icheckin.icheckin_android.utils.storage.AppDatabase
import my.com.icheckin.icheckin_android.utils.view.CustomProgressDialog

class AddAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        imageButton_Close.setOnClickListener {
            finish()
        }

        editText_ID.textWatcher {
            afterTextChanged { text ->
                if (text!!.isNotBlank()) {
                    if (editText_Otp.isNotBlank) {
                        enableButton(true)
                    }
                } else {
                    enableButton(false)
                }
            }
        }

        editText_Otp.textWatcher {
            afterTextChanged { text ->
                if (text!!.isNotBlank()) {
                    if (editText_ID.isNotBlank) {
                        enableButton(true)
                    }
                } else {
                    enableButton(false)
                }
            }
        }

        button_AddAccount.setOnClickListener {
            registerAccount(editText_ID.text.toString(), editText_Otp.text.toString())
        }
    }

    private fun enableButton(enable: Boolean) {
        button_AddAccount.isEnabled = enable
        val buttonDrawable = if (enable) R.drawable.rounded_button_secondary else R.drawable.rounded_button_disabled
        button_AddAccount.background = ContextCompat.getDrawable(applicationContext, buttonDrawable)
    }

    private fun registerAccount(username: String, otp: String) {
        val progressDialog = CustomProgressDialog(this)
        progressDialog.show()

        async {
            val db = AppDatabase.getDatabase(applicationContext)
            if (db.credentialDao().query(username) != null) {
                launch(UI) {
                    editText_ID.error = "$username already exists"
                    progressDialog.hide()
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                }
                return@async
            }
            val result = Izone.register(username, otp)
            launch(UI) {
                progressDialog.hide()
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
            when (result["status"] as Int) {
                0 -> {
                    db.credentialDao().insert(
                            Credential(username, result["device_id"] as String)
                    )
                    launch(UI) {
                        longToast("Success")
                        finish()
                    }
                }
                1 -> launch(UI) { editText_Otp.error = "Please try a new OTP" }
                2 -> launch(UI) { longToast("No internet connection") }
                3 -> launch(UI) { longToast("Not connected to uni wifi") }
            }
        }
    }
}
