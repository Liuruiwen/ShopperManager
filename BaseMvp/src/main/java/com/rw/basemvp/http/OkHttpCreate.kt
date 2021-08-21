package com.rw.basemvp.http

import android.os.Environment
import android.util.Log
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/*   
* @Author:      Amuser
* @CreateDate:   2019-12-18 17:31
*@Description: 
*/
object  OkHttpCreate {
    private val DEFAULT_CONNECT_TIMEOUT = 30//链接超时
    private val DEFAULT_WRITE_TIMEOUT = 30//写入超时
    private val DEFAULT_READ_TIMEOUT = 30//读取超时
    private var mHttpBuilder: OkHttpClient.Builder? = null

//     fun  getOkHttpInstance(): OkHttpCreate {
//        if (okHttpClient == null) {
//            okHttpClient = OkHttpCreate()
//        }
//
//        return okHttpClient as OkHttpCreate
//    }


    /**
     * 添加头部信息
     *
     * @return
     */
    fun addHttpHeader(): OkHttpCreate {

        /**
         * 设置头信息
         */
        val headerInterceptor = Interceptor { chain ->
            val request = chain.request()

                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")

                .addHeader("Accept-Encoding", "gzip, deflate")

                .addHeader("Connection", "keep-alive")

                .addHeader("Accept", "*/*")

                .addHeader("Cookie", "add cookies here")

                .build()

            chain.proceed(request)
        }
        this.mHttpBuilder!!.addInterceptor(headerInterceptor)
        return this
    }

    /***
     * 添加日志文件
     * @return
     */
    fun addHttpLogger(builder: OkHttpClient.Builder): OkHttpCreate {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("HttpLog", message)
            }


        })
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(loggingInterceptor)

        return this
    }


    /**
     * 设置超时时间
     *
     * @return
     */
    fun setHttpTimeOut(builder: OkHttpClient.Builder): OkHttpCreate {
        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.readTimeout(DEFAULT_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(DEFAULT_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)

        return this
    }

    /**
     * 设置缓存路径和大小
     */
    fun setCache(builder: OkHttpClient.Builder): OkHttpCreate {
        //缓存容量     缓存路径
        //缓存路径和大小
        val httpCacheDirectory = File(Environment.getExternalStorageDirectory(), "HttpCache")
        val cacheSize = 10 * 1024 * 1024
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        builder.cache(cache)

        return this
    }


    fun getHttpBuilder(): OkHttpClient.Builder {
        if (mHttpBuilder == null) {
            mHttpBuilder = OkHttpClient.Builder()
        }
        return mHttpBuilder as OkHttpClient.Builder
    }

    fun createOkHttpBuilder(): OkHttpClient.Builder {

        getHttpBuilder()
        addHttpLogger(mHttpBuilder!!)
        setHttpTimeOut(mHttpBuilder!!)
        return mHttpBuilder as OkHttpClient.Builder
    }




}