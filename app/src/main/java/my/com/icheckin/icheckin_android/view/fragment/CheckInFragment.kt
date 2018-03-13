package my.com.icheckin.icheckin_android.fragment


import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.textWatcher
import kotlinx.android.synthetic.main.fragment_check_in.*
import kotlinx.coroutines.experimental.async
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.utils.service.IcheckinService
import my.com.icheckin.icheckin_android.view.fragment.CheckInStatusCardView


/**
 * A simple [Fragment] subclass.
 */
class CheckInFragment : Fragment() {

    var icheckinService: IcheckinService? = null

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
            async {
                icheckinService!!.startCheckin(editText_Code.text.toString())
            }
        }

        recycleView_CheckinStatus.layoutManager = LinearLayoutManager(activity.applicationContext)
    }

    override fun onStart() {
        super.onStart()

        val service = Intent(context, IcheckinService::class.java)
        activity.startService(service)
        activity.bindService(service, serviceConnection, 0)
        activity.registerReceiver(broadcastReceiver, IntentFilter(IcheckinService.BROADCAST_ACTION))
    }

    fun loadStatus() {
        recycleView_CheckinStatus.adapter = CheckInStatusCardView(activity.applicationContext, icheckinService!!.status)
    }

    override fun onStop() {
        super.onStop()
        activity.unregisterReceiver(broadcastReceiver)
        if (icheckinService!!.running!!) {
            icheckinService!!.foreground()
        } else {
            activity.stopService(Intent(context, IcheckinService::class.java))
        }
        activity.unbindService(serviceConnection)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // textView_Status.text = icheckinService!!.status
            recycleView_CheckinStatus.adapter.notifyDataSetChanged()
        }
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as IcheckinService.IcheckinServiceBinder
            icheckinService = binder.service
            icheckinService!!.background()
            if (icheckinService!!.lastSeen) {
                icheckinService!!.initializeStatus()
            } else {
                if (!icheckinService!!.running!!) {
                    icheckinService!!.lastSeen = true
                }
            }
            loadStatus()
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    private fun enableButton(enable: Boolean) {
        button_CheckIn.isEnabled = enable
        val buttonDrawable = if (enable) R.drawable.rounded_button_primary else R.drawable.rounded_button_disabled
        button_CheckIn.background = ContextCompat.getDrawable(activity.applicationContext, buttonDrawable)
    }

}// Required empty public constructor
