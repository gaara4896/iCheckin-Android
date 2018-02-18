package my.com.icheckin.icheckin_android.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.IntentFor
import kotlinx.android.synthetic.main.fragment_account.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.utils.storage.AppDatabase
import my.com.icheckin.icheckin_android.view.AddAccountActivity
import my.com.icheckin.icheckin_android.view.ModifyAccountActivity
import my.com.icheckin.icheckin_android.view.fragment.AccountCardView


/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_NewAccount.setOnClickListener {
            startActivity(IntentFor<AddAccountActivity>(context))
        }

        loadStudent()
    }

    override fun onResume() {
        super.onResume()
        loadStudent()
    }

    private fun loadStudent() {
        val db = AppDatabase.getDatabase(activity.applicationContext)
        val students = db.studentDao().allStudent()
        recycleView_Account.layoutManager = LinearLayoutManager(activity.applicationContext)
        recycleView_Account.adapter = AccountCardView(activity.applicationContext, students) { student ->
            val intent = IntentFor<ModifyAccountActivity>(context)
            intent.putExtra("student", student)
            startActivity(intent)
        }
    }

}// Required empty public constructor
