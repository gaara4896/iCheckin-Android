package my.com.icheckin.icheckin_android.controller

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
        val response = Request.post(LOGIN_URL, data = payload)
        if (response.isSuccessful && response.priorResponse() != null) {
            return true
        }
        return false
    }
}