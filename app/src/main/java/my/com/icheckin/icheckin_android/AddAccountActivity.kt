package my.com.icheckin.icheckin_android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_add_account.*

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
        }
    }
}
