package com.rw.shoppermanager.ui.test

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rw.basemvp.news.service.HttpCreate
import com.rw.basemvp.news.model.BaseViewModel
import com.rw.basemvp.news.map.DataMap
import com.rw.basemvp.news.subscribeResult
import com.rw.basemvp.news.threadIo

/**
 * Created by Amuse
 * Date:2021/10/11.
 * Desc:
 */
class TestModel(application: Application) : BaseViewModel(application) {
    private val service:TestService by lazy {
        HttpCreate.createApiService(TestService::class.java,"https://www.wanandroid.com/")
    }

    var result=MutableLiveData<String>()

    fun getLogin(map:Map<String, String>){
        service.getLogin(map).threadIo().map (DataMap()).subscribeResult({
            result.value=it?.username
        },{
            result.value="请求失败"
        }).addBind()
    }

    data class LoginBean(
        val username:String,
        val password:String
    )
}