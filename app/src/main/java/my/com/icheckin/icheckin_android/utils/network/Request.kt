package my.com.icheckin.icheckin_android.utils.network

import khttp.responses.Response
import khttp.structures.cookie.CookieJar
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

/**
 * Created by gaara on 1/29/18.
 */
class Request {

    fun post(url: String, data: Map<String, String> = mapOf(), cookies: CookieJar = CookieJar()): Response {
        var response: Response? = null
        val responseFuture = async { khttp.post(url, data = data, cookies = cookies) }
        runBlocking { response = responseFuture.await() }
        return response!!
    }
}