package my.com.icheckin.icheckin_android.utils.network

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.*
import java.io.BufferedReader
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit

/**
 * Created by gaara on 1/29/18.
 */
object Request {

    fun get(url: String, data: Map<String, String>? = null, cookieJar: CookieJar? = null,
            timeout: Long = 2, unit: TimeUnit = TimeUnit.SECONDS): Response {

        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(timeout, unit)

        if (cookieJar != null) {
            clientBuilder.cookieJar(cookieJar)
        } else {
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            clientBuilder.cookieJar(JavaNetCookieJar(cookieManager))
        }

        val client = clientBuilder.build()

        val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
        if (data != null) {
            for (key in data.keys) {
                urlBuilder.addQueryParameter(key, data[key])
            }
        }
        val queryUrl = urlBuilder.build()

        val request = okhttp3.Request.Builder()
                .url(queryUrl)
                .get()
                .build()
        val responseFuture = async { client.newCall(request).execute() }
        return runBlocking { responseFuture.await() }
    }

    fun post(url: String, data: Map<String, String>? = null, cookieJar: CookieJar? = null,
             timeout: Long = 2, unit: TimeUnit = TimeUnit.SECONDS): Pair<Response, CookieJar> {

        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(timeout, unit)

        if (cookieJar != null) {
            clientBuilder.cookieJar(cookieJar)
        } else {
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            clientBuilder.cookieJar(JavaNetCookieJar(cookieManager))
        }

        val client = clientBuilder.build()

        val bodyBuilder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
        if (data != null) {
            for (key in data.keys) {
                bodyBuilder.addFormDataPart(key, data[key])
            }
        }
        val body = bodyBuilder.build()

        val request = okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build()
        val responseFuture = async { client.newCall(request).execute() }
        val response = runBlocking { responseFuture.await() }
        return Pair(response, client.cookieJar())
    }

    fun responseBodyReader(response: Response): String {
        val reader = BufferedReader(response.body()!!.charStream())
        val stringBuilder = StringBuilder()
        reader.forEachLine { line ->
            stringBuilder.append(line)
        }
/*        while (reader.){
            val line = reader.readLine() ?: break
            stringBuilder.append(line)
        }*/
        return stringBuilder.toString()
    }
}
