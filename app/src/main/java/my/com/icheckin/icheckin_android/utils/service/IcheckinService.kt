package my.com.icheckin.icheckin_android.utils.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import my.com.icheckin.icheckin_android.R
import my.com.icheckin.icheckin_android.controller.Izone
import my.com.icheckin.icheckin_android.model.entity.Credential
import my.com.icheckin.icheckin_android.utils.collection.MutablePair
import my.com.icheckin.icheckin_android.utils.storage.AppDatabase
import my.com.icheckin.icheckin_android.view.MainActivity

/**
 * Created by gaara on 2/24/18.
 */
class IcheckinService : Service() {

    val status: MutableList<MutablePair<Credential, Int?>> = mutableListOf()
    var running: Boolean? = null
    var lastSeen = true
    private val NOTIFICATION_ID = 1
    private val serviceBinder = IcheckinServiceBinder()
    private val broadcastIntent = Intent(BROADCAST_ACTION)

    companion object {
        const val BROADCAST_ACTION = "my.com.icheckin.icheckin_android.fragment"
    }

    inner class IcheckinServiceBinder : Binder() {
        internal val service: IcheckinService
            get() = this@IcheckinService
    }

    override fun onCreate() {
        super.onCreate()
        running = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return serviceBinder
    }

    fun startCheckin(code: String) {
        if (!running!!) {
            running = true
            lastSeen = false
            for (pair in status) {
                pair.second = -1
            }
            sendBroadcast(broadcastIntent)
            for (pair in status) {
                val credential = pair.first
                pair.second = Izone.checkin(applicationContext, credential, code)
                sendBroadcast(broadcastIntent)
            }
            running = false
            sendBroadcast(broadcastIntent)
        }
    }

    fun initializeStatus() {
        status.clear()
        val credentials = AppDatabase.getDatabase(applicationContext).credentialDao().allCredential()
        for (credential in credentials) {
            status.add(MutablePair(credential, null))
        }
    }

    fun foreground() {
        startForeground(NOTIFICATION_ID, createNotification())
    }

    fun background() {
        stopForeground(true)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(applicationContext, NotificationChannel.DEFAULT_CHANNEL_ID)
                .setContentTitle("Checking In...")
                .setContentText("Tap to return to Checkin")
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_icheckin_logo_round)
                .setContentIntent(
                        PendingIntent.getActivity(
                                this,
                                0,
                                Intent(this, MainActivity::class.java),
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )
                ).build()
    }

}