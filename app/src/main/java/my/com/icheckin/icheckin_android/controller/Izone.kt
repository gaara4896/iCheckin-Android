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

    private const val HEROKU_BACKEND = "https://icheckin-backend.herokuapp.com"
    private const val HEROKU_BACKEND_USER = "$HEROKU_BACKEND/api/user"

    fun register(username: String, otp: String, name: String): Map<String, Any> {

        try {
            val response = Request.get("$HEROKU_BACKEND_USER/$username")
            val jsonResponse = JSONObject(response.body()!!.string())
            when (response.code()) {
                200 -> {
                    val user = jsonResponse.getJSONObject("user")
                    return mapOf(
                            "status" to 4,
                            "device_id" to user["device_id"],
                            "source" to user["source"]
                    )
                }
            }
        } catch (e: IOException) {
            return mapOf("status" to 2)
        }

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
                    0 -> {
                        val userPayload = mapOf(
                                "device_id" to deviceId,
                                "username" to username,
                                "name" to name,
                                "source" to "ANDROID"
                        )
                        val herokuResponse = Request.post("$HEROKU_BACKEND_USER/", form = userPayload)["response"] as Response
                        return if (herokuResponse.isSuccessful) {
                            mapOf("status" to 0, "device_id" to deviceId)
                        } else {
                            mapOf("status" to 1)
                        }
                    }
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
                    "source" to credential.source,
                    "rssi" to wifiInfo.rssi,
                    "icheckincode" to code
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