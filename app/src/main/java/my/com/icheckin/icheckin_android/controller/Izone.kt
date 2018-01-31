package my.com.icheckin.icheckin_android.controller

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import my.com.icheckin.icheckin_android.utils.network.Request
import okhttp3.CookieJar
import java.io.IOException

/**
 * Created by gaara on 1/29/18.
 */
object Izone {
    private val IZONE_URL = "https://izone.sunway.edu.my"
    val LOGIN_URL = "$IZONE_URL/login"
    private val CHECKIN_URL = "$IZONE_URL/icheckin/iCheckinNowWithCode"
    private val WIFI_URL = "https://icheckin.sunway.edu.my/otp/CheckIn/isAlive/CuNv9UV2rXg4WtAsXUPNptg6gWQTZ52w"

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

    fun checkin(username: String, password: String, code: String): String {

        val pair = login(username, password)
        if (!pair.first) {
            return "Invalid credentials"
        }

        try {
            Request.get(WIFI_URL)
        } catch (e: IOException) {
            return "Not connected to University Wi-Fi"
        }

        val response = Request.post(CHECKIN_URL, data = mapOf("checkin_code" to code),
                cookieJar = pair.second).first
        if (response.isSuccessful) {
            val textFuture = async { Request.responseBodyReader(response) }
            val text = runBlocking { textFuture.await() }
            if ("Checkin code not valid." in text || "The specified URL cannot be found." in text) {
                return "Invalid code"
            } else if ("You cannot check in to a class you are not a part of." in text) {
                return "Wrong class"
            } else if ("You have already checked in" in text) {
                return "Already checked-in"
            }
            return "Successful check-in"
        }
        return "No internet connection"
    }
}