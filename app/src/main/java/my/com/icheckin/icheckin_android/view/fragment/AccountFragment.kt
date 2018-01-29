package my.com.icheckin.icheckin_android.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_account.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.view.AddAccountActivity


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
            startActivity(Intent(context, AddAccountActivity::class.java))
            activity.finish()
        }
    }

}// Required empty public constructor
