package my.com.icheckin.icheckin_android.controller

import android.content.Context
import com.pawegio.kandroid.wifiManager
import my.com.icheckin.icheckin_android.model.entity.Credential
import my.com.icheckin.icheckin_android.utils.network.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * Created by gaara on 1/29/18.
 */
object Izone {
    private const val ICHECKIN_URL = "https://icheckin.sunway.edu.my"
    private const val ICHECKIN_REGISTER_URL = "$ICHECKIN_URL/registration.php"
    private const val ICHECKIN_CHECKIN_URL = "$ICHECKIN_URL/checkin_withcode.php"
    private const val ICHECKIN_WIFI_URL = "$ICHECKIN_URL/otp/CheckIn/isAlive/CuNv9UV2rXg4WtAsXUPNptg6gWQTZ52w"

    fun register(username: String, otp: String): Map<String, Any> {

        val deviceId = UUID.randomUUID().toString()

        val payload = JSONObject(mapOf(
                "device_id" to deviceId,
                "student_uid" to username,
                "otp_code" to otp,
                "source" to "ANDROID"
        ))

        try {
            val response = Request.post(ICHECKIN_REGISTER_URL, json = payload)["response"] as Response
            if (response.isSuccessful) {
                val jsonResponse = JSONObject(response.body()!!.string())
                when (jsonResponse["status"] as Int) {
                    0 -> return mapOf("status" to 0, "device_id" to deviceId)
                    1 -> return mapOf("status" to 1)
                }
            }
        } catch (e: IOException) {
            try {
                Request.get(ICHECKIN_WIFI_URL)
            } catch (e: IOException) {
                return mapOf("status" to 3)
            }
        }

        return mapOf("status" to 2)
    }

    fun checkin(context: Context, credential: Credential, code: String): Int {
        try {
            val wifiInfo = context.wifiManager!!.connectionInfo
            val payload = JSONObject(mapOf(
                    "device_id" to credential.deviceId,
                    "ssid" to wifiInfo.ssid.replace("\"", ""),
                    "bssid" to wifiInfo.bssid,
                    "source" to "ANDROID",
                    "rssi" to wifiInfo.rssi,
                    "code" to code
            ))

            val response = Request.post(ICHECKIN_CHECKIN_URL, json = payload)["response"] as Response
            if (response.isSuccessful) {
                val jsonResponse = JSONObject(response.body()!!.string())
                return jsonResponse["status"] as Int
            }
        } catch (e: IOException) {
            try {
                Request.get(ICHECKIN_WIFI_URL)
            } catch (e: IOException) {
                return 8
            }
        }
        return 9
    }
}