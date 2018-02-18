package my.com.icheckin.icheckin_android.view.fragment

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.visible
import kotlinx.android.synthetic.main.cardview_account.view.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.model.entity.Student

/**
 * Created by gaara on 1/31/18.
 */
class AccountCardView(val context: Context, private val students: MutableList<Student>, private val listener: (Student) -> Unit) : RecyclerView.Adapter<AccountCardView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountCardView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_account, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AccountCardView.ViewHolder, position: Int) {
        holder.bindItems(students[position], position, listener)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(student: Student, position: Int, listener: (Student) -> Unit) = with(itemView) {
            itemView.setOnClickListener {
                listener.invoke(student)
            }
            itemView.textView_ID.text = student.username
            if (position == 0) itemView.imageView_Crown.visible = true
        }
    }

}