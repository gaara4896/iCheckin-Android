package my.com.icheckin.icheckin_android

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.bottom_navigation.*
import my.com.icheckin.icheckin_android.Fragment.AccountFragment
import my.com.icheckin.icheckin_android.Fragment.CheckInFragment

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_Accounts -> {
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AccountFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_CheckIn -> {
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, CheckInFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AccountFragment()).commit()
    }
}
