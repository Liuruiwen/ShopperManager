package com.ruiwenliu.glide.library

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 * Created by Amuse
 * Date:2020/3/18.
 * Desc:
 */
class ProgressInterceptor :Interceptor{

    companion object {
        val LISTENER_MAP: MutableMap<String, ProgressListener> =
            HashMap<String, ProgressListener>()
        fun addListener(url: String, listener: ProgressListener) {
            LISTENER_MAP[url] = listener
        }

        fun removeListener(url: String?) {
            LISTENER_MAP.remove(url)
        }
    }





    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val url = request.url().toString()
        val body = response.body()
        return response.newBuilder().body(ProgressResponseBody(url, body)).build()
    }
}