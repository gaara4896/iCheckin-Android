package my.com.icheckin.icheckin_android.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.com.icheckin.icheckin_android.R


/**
 * A simple [Fragment] subclass.
 */
class CheckInFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_check_in, container, false)
    }

}// Required empty public constructor
