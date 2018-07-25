package my.com.icheckin.icheckin_android.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.bottom_navigation.*
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Advertisement
import my.com.icheckin.icheckin_android.fragment.AccountFragment
import my.com.icheckin.icheckin_android.fragment.CheckInFragment

class MainActivity : AppCompatActivity() {

    lateinit var interstitialAd: InterstitialAd

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

        interstitialAd = InterstitialAd(applicationContext)
        interstitialAd.adUnitId = Advertisement.INTERSTITIALS_ADS_ID
        interstitialAd.loadAd(AdRequest.Builder().build())

        interstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                interstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AccountFragment()).commit()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes") { _, _ ->
                    if (interstitialAd.isLoaded) interstitialAd.show()
                    super.onBackPressed()
                }
                .setNegativeButton("No") { _, _ -> }
                .setNeutralButton("Facebook Page") { _, _ ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/icheckinproject")))
                    finish()
                }
                .setCancelable(false)
                .create()
                .show()
    }
}
