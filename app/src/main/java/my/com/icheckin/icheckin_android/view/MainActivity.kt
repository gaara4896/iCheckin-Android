package my.com.icheckin.icheckin_android.view

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.bottom_navigation.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Advertisement
import my.com.icheckin.icheckin_android.fragment.AccountFragment
import my.com.icheckin.icheckin_android.fragment.CheckInFragment

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

        MobileAds.initialize(this, Advertisement.ADMOB_APP_ID)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AccountFragment()).commit()
    }
}
