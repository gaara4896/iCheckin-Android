package my.com.icheckin.icheckin_android.utils.network

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.*
import org.json.JSONObject
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
                .addInterceptor(UserAgentInterceptor())

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

    fun post(url: String, form: Map<String, String>? = null, json: JSONObject? = null,
             cookieJar: CookieJar? = null, timeout: Long = 2,
             unit: TimeUnit = TimeUnit.SECONDS): Map<String, Any> {

        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(timeout, unit)
                .addInterceptor(UserAgentInterceptor())

        if (cookieJar != null) {
            clientBuilder.cookieJar(cookieJar)
        } else {
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            clientBuilder.cookieJar(JavaNetCookieJar(cookieManager))
        }

        val requestBuilder = okhttp3.Request.Builder()
                .url(url)

        if (json != null) {
            requestBuilder.post(
                    RequestBody.create(
                            MediaType.parse("application/json; charset=utf-8"),
                            json.toString()
                    )
            )
        } else if (form != null) {
            val bodyBuilder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)

            for (key in form.keys) {
                bodyBuilder.addFormDataPart(key, form[key]!!)
            }

            requestBuilder.post(bodyBuilder.build())
        }

        val client = clientBuilder.build()
        val responseFuture = async { client.newCall(requestBuilder.build()).execute() }
        val response = runBlocking { responseFuture.await() }

        return mapOf("response" to response, "cookie" to client.cookieJar())
    }

    fun delete(url: String, data: Map<String, String>? = null, cookieJar: CookieJar? = null,
               timeout: Long = 2, unit: TimeUnit = TimeUnit.SECONDS): Response {

        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(timeout, unit)
                .addInterceptor(UserAgentInterceptor())

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
                .delete()
                .build()
        val responseFuture = async { client.newCall(request).execute() }
        return runBlocking { responseFuture.await() }
    }

    fun responseBodyReader(response: Response): String {
        val reader = BufferedReader(response.body()!!.charStream())
        val stringBuilder = StringBuilder()
        reader.forEachLine { line ->
            stringBuilder.append(line)
        }
        return stringBuilder.toString()
    }
}
