package com.rw.shoppermanager.ui.test

import com.rw.basemvp.bean.MessageBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * Created by Amuse
 * Date:2021/10/11.
 * Desc:
 */
interface TestService {
    @POST("/user/login")
    fun Login( @Body req: RequestBody):Observable<MessageBean<TestBean>>

    @POST("/user/login")
    fun getLogin( @QueryMap map:Map<String, String>):Observable<MessageBean<TestBean>>
}