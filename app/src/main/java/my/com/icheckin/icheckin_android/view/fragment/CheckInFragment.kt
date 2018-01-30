package my.com.icheckin.icheckin_android.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.longToast
import kotlinx.android.synthetic.main.fragment_check_in.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Izone
import my.com.icheckin.icheckin_android.model.Student
import my.com.icheckin.icheckin_android.utils.database.Database
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

        button_CheckIn.setOnClickListener {
            val students = Database.query(activity.applicationContext, Student())
            for (student in students) {
                try {
                    val result = Izone.checkin(student.username!!, student.password!!, "12345")
                    longToast(result)
                } catch (e: IOException) {
                    longToast("No internet connection")
                }
            }
        }
    }

}// Required empty public constructor
