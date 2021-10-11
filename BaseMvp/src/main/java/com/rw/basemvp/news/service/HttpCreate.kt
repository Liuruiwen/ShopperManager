package com.rw.basemvp.news.service

import com.rw.basemvp.http.OkHttpCreate
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Amuse
 * Date:2021/10/8.
 * Desc:
 */
object HttpCreate {

     fun <T> createApiService(service: Class<T>,baseUrl:String): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpCreate.createOkHttpBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(service)
    }





}



