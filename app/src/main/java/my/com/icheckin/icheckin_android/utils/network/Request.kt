package my.com.icheckin.icheckin_android.utils.network

import android.os.AsyncTask
import com.pawegio.kandroid.d
import khttp.responses.Response
import khttp.structures.cookie.CookieJar
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

/**
 * Created by gaara on 1/29/18.
 */
/*class Request() : AsyncTask<Any, Void, Response>() {

    companion object {
        enum class Method {
            GET, POST, PUT, DELETE
        }
    }

    override fun doInBackground(params: Array<Any>): Response {
        when (params[0] as Method) {
            Method.GET -> {
                return khttp.get(params[1] as String, data = params[2] as Map<String, String>?, cookies = params[3] as CookieJar?)
            }
            Method.POST -> {
                return khttp.post(params[1] as String, data = params[2] as Map<String, String>?, cookies = params[3] as CookieJar?)
            }
            Method.PUT -> {
                return khttp.put(params[1] as String, data = params[2] as Map<String, String>?, cookies = params[3] as CookieJar?)
            }
            Method.DELETE -> {
                return khttp.delete(params[1] as String, data = params[2] as Map<String, String>?, cookies = params[3] as CookieJar?)
            }
        }
    }
}*/
object Request {

    fun post(url: String, data: Map<String, String>? = null, cookies: CookieJar? = null): Response {

        d("Start")
        val responseFuture = async { khttp.post(url, data = data, cookies = cookies) }
        val response = runBlocking { responseFuture.await() }
        d("Finish")
        return response
    }
}
