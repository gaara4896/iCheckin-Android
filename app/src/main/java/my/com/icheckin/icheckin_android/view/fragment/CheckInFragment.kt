package my.com.icheckin.icheckin_android.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.pawegio.kandroid.textWatcher
import kotlinx.android.synthetic.main.fragment_check_in.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Izone
import my.com.icheckin.icheckin_android.utils.storage.AppDatabase
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class CheckInFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_check_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        editText_Code.textWatcher {
            afterTextChanged { text ->
                if (text!!.isNotBlank()) enableButton(true) else enableButton(false)
            }
        }

        button_CheckIn.setOnClickListener {
            textView_Status.text = ""
            enableButton(false)
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            async {
                val students = AppDatabase.getDatabase(activity.applicationContext).studenDao().allStudent()
                val code = editText_Code.text.toString()
                for (student in students) {
                    launch(UI) { textView_Status.text = "${textView_Status.text.toString()}Checking in for ${student.username}\n" }
                    try {
                        val result = Izone.checkin(student.username!!, student.password(activity.applicationContext), code)
                        launch(UI) { textView_Status.text = "${textView_Status.text.toString()}$result\n" }
                    } catch (e: IOException) {
                        launch(UI) { textView_Status.text = "${textView_Status.text.toString()}No internet connection\n" }
                    }
                }
                launch(UI) {
                    textView_Status.text = "${textView_Status.text.toString()}Finish Checkin\n"
                    enableButton(true)
                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
        }
    }

    private fun enableButton(enable: Boolean) {
        button_CheckIn.isEnabled = enable
        val buttonDrawable = if (enable) R.drawable.rounded_button_primary else R.drawable.rounded_button_disabled
        button_CheckIn.background = ContextCompat.getDrawable(activity.applicationContext, buttonDrawable)
    }

}// Required empty public constructor
