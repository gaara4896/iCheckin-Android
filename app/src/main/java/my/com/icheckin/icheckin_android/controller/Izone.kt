package my.com.icheckin.icheckin_android.controller

import com.pawegio.kandroid.d
import kotlinx.coroutines.experimental.runBlocking
import my.com.icheckin.icheckin_android.utils.network.Request

/**
 * Created by gaara on 1/29/18.
 */
object Izone {
    private val IZONE_URL = "https://izone.sunway.edu.my"
    val LOGIN_URL = "$IZONE_URL/login"
    private val CHECKIN_URL = "$IZONE_URL/icheckin/iCheckinNowWithCode"
    private val WIFI_URL = "https://icheckin.sunway.edu.my/otp/CheckIn/isAlive/CuNv9UV2rXg4WtAsXUPNptg6gWQTZ52w"

    fun login(username: String, password: String): Boolean {
        val payload = mapOf(
                "form_action" to "submitted",
                "student_uid" to username,
                "password" to password)
        //val response = Request().execute(Request.Companion.Method.POST, LOGIN_URL, payload, null).get()
        val response = Request.post(LOGIN_URL, data=payload)
        if (response.statusCode == 200) {
            if (response.history.isNotEmpty()) {
                d("Success")
                return true
            }
            d("Fail")
            return false
        } else {
            d("${response.statusCode}")
            return false
        }
    }
}