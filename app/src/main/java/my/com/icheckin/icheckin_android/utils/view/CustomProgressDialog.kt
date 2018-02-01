package my.com.icheckin.icheckin_android.utils.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.custom_progress_dialog.*
import my.com.icheckin.icheckin_android.R

/**
 * Created by gaara on 2/1/18.
 */
class CustomProgressDialog(context: Context, val text: String) : Dialog(context) {

    constructor(context: Context) : this(context, "Processing...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_progress_dialog)
        textView_text.text = text
        super.setCanceledOnTouchOutside(false)
    }
}