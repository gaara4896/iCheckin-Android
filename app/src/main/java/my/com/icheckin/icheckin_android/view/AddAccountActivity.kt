package my.com.icheckin.icheckin_android.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.pawegio.kandroid.d
import com.pawegio.kandroid.longToast
import com.pawegio.kandroid.textWatcher
import kotlinx.android.synthetic.main.activity_add_account.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Izone
import my.com.icheckin.icheckin_android.model.Student
import my.com.icheckin.icheckin_android.utils.database.Database
import ninja.sakib.pultusorm.core.PultusORMCondition
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

class AddAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        imageButton_Close.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        editText_ID.textWatcher {
            afterTextChanged { text ->
                if (text!!.isNotBlank()) {
                    if (editText_Password.isNotBlank) {
                        enableButton(true)
                    }
                } else {
                    enableButton(false)
                }
            }
        }

        editText_Password.textWatcher {
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
            insertAccount(editText_ID.text.toString(), editText_Password.text.toString())
        }
    }

    private fun enableButton(enable: Boolean) {
        button_AddAccount.isEnabled = enable
        val buttonDrawable = if (enable) R.drawable.rounded_button_secondary else R.drawable.rounded_button_disabled
        button_AddAccount.background = ContextCompat.getDrawable(applicationContext, buttonDrawable)
    }

    private fun insertAccount(username: String, password: String) {
        if (!Database.query<Student>(applicationContext, Student(),
                PultusORMCondition.Builder()
                        .eq("username", username)
                        .build()).isEmpty()) {
            longToast("Account $username already exists.")
            return
        }
        val success = Izone.login(username, password)
        if(success){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            longToast("Failed")
        }
/*        val payload = mapOf(
                "form_action" to "submitted",
                "student_uid" to username,
                "password" to password)
        val responseFuture = async { khttp.post(Izone.LOGIN_URL, data = payload) }
        val response = runBlocking { responseFuture.await() }
        if (response.statusCode == 200) {
            if (response.history.isNotEmpty()) {
                d("Success")
                longToast("Success")
            }
            d("Fail")
            d(response.headers.toString())
            longToast("Fail")
        } else {
            d("${response.statusCode}")
            longToast("Fail")
        }*/
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
