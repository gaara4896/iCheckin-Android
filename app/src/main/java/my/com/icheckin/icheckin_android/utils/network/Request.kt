package my.com.icheckin.icheckin_android.utils.network

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.CookieJar
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Response

/**
 * Created by gaara on 1/29/18.
 */
object Request {

    fun post(url: String, data: Map<String, String>? = null, cookies: CookieJar? = null): Response {

        val clientBuilder = OkHttpClient.Builder()
        if (cookies != null) clientBuilder.cookieJar(cookies)
        val client = clientBuilder.build()

        val bodyBuilder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("form_action", "submitted")
        for (key in data!!.keys) {
            bodyBuilder.addFormDataPart(key, data!![key])
        }
        val body = bodyBuilder.build()

        val request = okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build()
        val responseFuture = async { client.newCall(request).execute() }
        return runBlocking { responseFuture.await() }
    }
}
