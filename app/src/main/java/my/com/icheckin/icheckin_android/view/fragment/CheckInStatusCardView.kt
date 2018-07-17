package my.com.icheckin.icheckin_android.view.fragment

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.cardview_checkin_status.view.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.model.entity.Credential
import my.com.icheckin.icheckin_android.utils.collection.MutablePair

/**
 * Created by gaara on 3/13/18.
 */
class CheckInStatusCardView(val context: Context, private val status: MutableList<MutablePair<Credential, Int?>>) : RecyclerView.Adapter<CheckInStatusCardView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckInStatusCardView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_checkin_status, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CheckInStatusCardView.ViewHolder, position: Int) {
        holder.bindItems(context, status[position])
    }

    override fun getItemCount(): Int {
        return status.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(context: Context, status: MutablePair<Credential, Int?>) = with(itemView) {
            val credential = status.first
            val state = status.second

            textView_ID.text = credential.username

            textView_Status.text = ""

            textView_Pending.visibility = View.GONE
            imageView_Status.visibility = View.GONE
            progressBar_Status.visibility = View.GONE

            if (state == null) {
                textView_Pending.visibility = View.VISIBLE
            } else if (state == -1) {
                progressBar_Status.visibility = View.VISIBLE
                textView_Status.text = "Checking in"
                textView_Status.setTextColor(Color.BLACK)
            } else {
                imageView_Status.visibility = View.VISIBLE
                if (state == 0 || state == 7) {
                    textView_Status.setTextColor(ContextCompat.getColor(context, R.color.successText))
                    imageView_Status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_success))
                    when (state) {
                        0 -> textView_Status.text = "Checked in"
                        7 -> textView_Status.text = "Already checkin"
                    }
                } else {
                    textView_Status.setTextColor(ContextCompat.getColor(context, R.color.failureText))
                    imageView_Status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_failed))
                    when (state) {
                        1 -> textView_Status.text = "Invalid BSSID"
                        2 -> textView_Status.text = "Check in only available from 0800 to 1800"
                        3 -> textView_Status.text = "Credential not registered, please register again"
                        4 -> textView_Status.text = "Expired code"
                        5 -> textView_Status.text = "Invalid code"
                        6 -> textView_Status.text = "Check in fail"
                        8 -> textView_Status.text = "Not connected to uni wifi"
                        9 -> textView_Status.text = "No internet connection"
                    }
                }
            }
        }
    }

}