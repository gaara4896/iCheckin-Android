package my.com.icheckin.icheckin_android.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_add_account.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.model.Student
import my.com.icheckin.icheckin_android.utils.database.Database

class AddAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        imageButton_Close.setOnClickListener {
            finish()
        }

        button_AddAccount.setOnClickListener {
            if (TextUtils.isEmpty(editText_ID.text)) {
                editText_ID.error = "ID cannot be null"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(editText_Password.text)) {
                editText_Password.error = "ID cannot be null"
                return@setOnClickListener
            }

            val student = Student()
            student.init(editText_ID.text.toString(), editText_Password.text.toString())
            Database.insert(applicationContext, student)
        }
    }
}
