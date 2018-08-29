package my.com.icheckin.icheckin_android.utils.network

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
                .header("User-Agent", "")
                .build()

        return chain.proceed(newRequest)
    }

}