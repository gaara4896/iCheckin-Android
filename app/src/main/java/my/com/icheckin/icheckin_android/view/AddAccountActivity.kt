package my.com.icheckin.icheckin_android.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.pawegio.kandroid.longToast
import com.pawegio.kandroid.textWatcher
import com.pawegio.kandroid.wtf
import kotlinx.android.synthetic.main.activity_add_account.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Izone
import my.com.icheckin.icheckin_android.model.Student
import my.com.icheckin.icheckin_android.utils.database.Database
import ninja.sakib.pultusorm.core.PultusORMCondition
import java.io.IOException

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
            editText_ID.error = "$username already exists"
            return
        }
        try {
            if (Izone.login(username, password).first) {
                val student = Student()
                student.init(username, password)
                Database.insert(applicationContext, student)
                longToast("Success")
                finish()
            } else {
                editText_Password.error = "Wrong password"
            }
        } catch (e: IOException) {
            wtf(e.toString())
            longToast("No internet connection")
        }

    }
}
