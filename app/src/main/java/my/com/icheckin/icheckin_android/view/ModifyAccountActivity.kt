package my.com.icheckin.icheckin_android.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.pawegio.kandroid.textWatcher
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.activity_modify_account.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.model.entity.Credential
import my.com.icheckin.icheckin_android.utils.storage.AppDatabase

class ModifyAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_account)

        val db = AppDatabase.getDatabase(applicationContext)
        val credential = intent.getSerializableExtra("credential") as Credential

        textView_ID.text = credential.username
        editText_Name.setText(credential.name)

        editText_Name.textWatcher {
            afterTextChanged { text ->
                if (text!!.isNotBlank()) enableButton(true) else enableButton(false)
            }
        }

        button_ModifyAccount.setOnClickListener {
            credential.name = editText_Name.text.toString()
            db.credentialDao().update(credential)
            toast("Update successfully")
            finish()
        }

        imageButton_Delete.setOnClickListener {
            db.credentialDao().delete(credential)
            toast("${credential.username} deleted")
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
