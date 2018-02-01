package my.com.icheckin.icheckin_android.fragment


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import com.pawegio.kandroid.IntentFor
import com.pawegio.kandroid.longToast
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Izone
import my.com.icheckin.icheckin_android.model.Student
import my.com.icheckin.icheckin_android.utils.database.Database
import my.com.icheckin.icheckin_android.utils.view.CustomProgressDialog
import my.com.icheckin.icheckin_android.view.AddAccountActivity
import my.com.icheckin.icheckin_android.view.fragment.AccountCardView
import ninja.sakib.pultusorm.core.PultusORMCondition
import ninja.sakib.pultusorm.core.PultusORMUpdater


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
        val students = Database.query<Student>(activity.applicationContext, Student())
        recycleView_Account.layoutManager = LinearLayoutManager(activity.applicationContext)
        recycleView_Account.adapter = AccountCardView(activity.applicationContext, students) { student ->
            val editText = EditText(activity.window.context)
            editText.hint = "New Password"
            editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            AlertDialog.Builder(activity.window.context)
                    .setCancelable(true)
                    .setTitle("${student.username}")
                    .setView(editText)
                    .setPositiveButton("Change Password", { _, _ ->
                        val password = editText.text.toString()
                        if (password.isBlank()) {
                            longToast("New password cannot be blank")
                            return@setPositiveButton
                        }
                        val progressDialog = CustomProgressDialog(activity.window.context)
                        progressDialog.show()
                        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        async {
                            if (Izone.login(student.username!!, password).first) {
                                val success = Database.update(activity.applicationContext, Student(),
                                        PultusORMUpdater.Builder()
                                                .condition(PultusORMCondition.Builder()
                                                        .eq("username", student.username!!)
                                                        .build())
                                                .set("password", password)
                                                .build())
                                launch(UI) {
                                    progressDialog.hide()
                                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                                    if (success) longToast("Update successfully") else longToast("Failed")
                                }
                            } else {
                                launch(UI) {
                                    progressDialog.hide()
                                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                                    longToast("Wrong password")
                                }
                            }
                        }

                    })
                    .setNeutralButton("Delete", { _, _ ->
                        val success = Database.delete(activity.applicationContext, Student(),
                                PultusORMCondition.Builder()
                                        .eq("username", student.username!!)
                                        .build()
                        )
                        students.remove(student)
                        recycleView_Account.adapter.notifyDataSetChanged()
                        if (success) longToast("${student.username} deleted") else longToast("Fail to delete ${student.username}")
                    })
                    .setNegativeButton("Cancel", { _, _ -> })
                    .show()
        }
    }

}// Required empty public constructor
