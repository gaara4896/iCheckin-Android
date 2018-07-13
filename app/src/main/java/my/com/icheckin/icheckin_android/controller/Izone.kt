package my.com.icheckin.icheckin_android.controller

import com.pawegio.kandroid.d
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import my.com.icheckin.icheckin_android.utils.network.Request
import okhttp3.CookieJar
import java.io.IOException
import java.util.*

/**
 * Created by gaara on 1/29/18.
 */
object Izone {
    private val IZONE_URL = "https://izone.sunway.edu.my"
    private val ICHECKIN_URL = "https://icheckin.sunway.edu.my"
    private val ICHECKIN_REGISTER_URL = "$ICHECKIN_URL/registration.php"
    val LOGIN_URL = "$IZONE_URL/login"
    private val CHECKIN_URL = "$IZONE_URL/icheckin/iCheckinNowWithCode"
    private val WIFI_URL = "https://icheckin.sunway.edu.my/otp/CheckIn/isAlive/CuNv9UV2rXg4WtAsXUPNptg6gWQTZ52w"

    fun register(username: String, otp: String): Boolean {
/*        val payload = mapOf(
                "device_id" to UUID.randomUUID().toString(),
                "student_uid" to username,
                "otp_code" to otp,
                "source" to "ANDROID"
        )*/
        val payload = "{" +
                "\"device_id\": \"" + UUID.randomUUID().toString() + "\"," +
                "\"student_uid\": \"" + username + "\"," +
                "\"otp_code\": \"" + otp + "\"," +
                "\"source\": \"ANDROID\"" +
                "}"

        d(payload)

        val response = Request.post_json(ICHECKIN_REGISTER_URL, data = payload)
        if (response.isSuccessful){
            d(response.body()!!.string())
        }
        return true
    }

    fun login(username: String, password: String): Pair<Boolean, CookieJar> {

        val payload = mapOf(
                "form_action" to "submitted",
                "student_uid" to username,
                "password" to password)

        val pair = Request.post(LOGIN_URL, data = payload)
        val response = pair.first
        if (response.isSuccessful && response.priorResponse() != null) {
            return Pair(true, pair.second)
        }
        return Pair(false, pair.second)
    }

    fun checkin(username: String, password: String, code: String): Int {

        try {
            val pair = login(username, password)
            if (!pair.first) {
                return 2
            }

            try {
                Request.get(WIFI_URL)
            } catch (e: IOException) {
                return 3
            }

            val response = Request.post(CHECKIN_URL, data = mapOf("checkin_code" to code),
                    cookieJar = pair.second).first
            if (response.isSuccessful) {
                val textFuture = async { Request.responseBodyReader(response) }
                val text = runBlocking { textFuture.await() }
                if ("Checkin code not valid." in text || "The specified URL cannot be found." in text) {
                    return 4
                } else if ("You cannot check in to a class you are not a part of." in text) {
                    return 5
                } else if ("You have already checked in" in text) {
                    return 6
                }
                return 7
            }
            return 1
        } catch (e: IOException) {
            return 1
        }
    }
}