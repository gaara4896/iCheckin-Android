package my.com.icheckin.icheckin_android.view.fragment

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.visible
import kotlinx.android.synthetic.main.cardview_account.view.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.model.entity.Credential

/**
 * Created by gaara on 1/31/18.
 */
class AccountCardView(val context: Context, private val credentials: MutableList<Credential>, private val listener: (Credential) -> Unit) : RecyclerView.Adapter<AccountCardView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountCardView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_account, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AccountCardView.ViewHolder, position: Int) {
        holder.bindItems(credentials[position], position, listener)
    }

    override fun getItemCount(): Int {
        return credentials.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(credential: Credential, position: Int, listener: (Credential) -> Unit) = with(itemView) {
            itemView.setOnClickListener {
                listener.invoke(credential)
            }
            itemView.textView_ID.text = credential.username
            itemView.textView_Name.text = credential.name!!
            itemView.imageView_Crown.visible = position == 0
        }
    }

}