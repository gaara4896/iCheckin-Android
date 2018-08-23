package my.com.icheckin.icheckin_android.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
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
import my.com.icheckin.icheckin_android.model.entity.Credential
import my.com.icheckin.icheckin_android.utils.storage.AppDatabase
import my.com.icheckin.icheckin_android.utils.view.CustomProgressDialog

class ModifyAccountActivity : AppCompatActivity() {

    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_account)

        progressDialog = CustomProgressDialog(this, "Deleting...")

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
            AlertDialog.Builder(this)
                    .setTitle("Delete credential")
                    .setMessage("Do you want to remove credential from server?")
                    .setPositiveButton("Remove from server") { _, _ ->
                        progressDialog.show()
                        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        async {
                            Izone.delete(credential)
                            launch(UI) {
                                progressDialog.hide()
                                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                                db.credentialDao().delete(credential)
                                toast("${credential.username} deleted")
                                finish()
                            }
                        }
                    }
                    .setNegativeButton("This device only") { _, _ ->
                        db.credentialDao().delete(credential)
                        toast("${credential.username} deleted")
                        finish()
                    }
                    .setCancelable(false)
                    .create()
                    .show()
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
