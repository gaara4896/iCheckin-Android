package my.com.icheckin.icheckin_android.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.pawegio.kandroid.textWatcher
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.activity_modify_account.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Izone
import my.com.icheckin.icheckin_android.model.entity.Student
import my.com.icheckin.icheckin_android.utils.storage.AppDatabase
import my.com.icheckin.icheckin_android.utils.view.CustomProgressDialog

class ModifyAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_account)

        val db = AppDatabase.getDatabase(applicationContext)
        val student = intent.getSerializableExtra("student") as Student

        textView_ID.text = student.username!!
        editText_Password.setText(student.password(applicationContext))

        editText_Password.textWatcher {
            afterTextChanged { text ->
                if (text!!.isNotBlank()) enableButton(true) else enableButton(false)
            }
        }

        button_ModifyAccount.setOnClickListener {
            val password = editText_Password.text.toString()
            val progressDialog = CustomProgressDialog(this)
            progressDialog.show()
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            async {
                if (Izone.login(student.username!!, password).first) {
                    student.password(applicationContext, password)
                    db.studentDao().update(student)
                    launch(UI) {
                        progressDialog.hide()
                        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        toast("Update successfully")
                        finish()
                    }
                } else {
                    launch(UI) {
                        progressDialog.hide()
                        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        editText_Password.error = "Wrong password"
                    }
                }
            }
        }

        imageButton_Delete.setOnClickListener {
            db.studentDao().delete(student)
            toast("${student.username!!} deleted")
            finish()
        }

        imageButton_Close.setOnClickListener {
            finish()
        }

    }

    private fun enableButton(enable: Boolean) {
        button_ModifyAccount.isEnabled = enable
        val buttonDrawable = if (enable) R.drawable.rounded_button_secondary else R.drawable.rounded_button_disabled
        button_ModifyAccount.background = ContextCompat.getDrawable(applicationContext, buttonDrawable)
    }
}
