package com.rw.basemvp.presenter

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.rw.basemvp.http.HttpApi
import com.rw.basemvp.http.OkHttpCreate
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.LinkedHashMap

/*   
* @Author:      Amuser
* @CreateDate:   2019-12-18 16:37
*@Description: 
*/
abstract class RetrofitPresenter<V : BaseView> : WrapperPresenter<V>() {
    private var RETRY_COUNT:Int = 0//请求失败重连次数
    private var httpApi: HttpApi? = null

  private  var disMap :MutableMap<Any, DisposableObserver<*>>? = null
    private  var ppppp :MutableMap<Any, String>? = null

    /**
     * 创建自定义域名服务
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T> createService(service: Class<T>, baseUrl: String): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpCreate.createOkHttpBuilder().build())
            .build()
            .create(service)
    }

    /**
     * 创建服务
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    private fun <T> createApiService(service: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(OkHttpCreate.createOkHttpBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(service)
    }




    /**
     * 设置订阅 和 所在的线程环境
     */
    fun<T> toSubscribe(
        observable: Observable<T>,
        disposableObserver: DisposableObserver<T>?,
        position: Int) {
        disposableObserver?.apply {
            getDisMap()?.put(position, disposableObserver)
            Log.d("有没有数据：",""+getDisMap()?.size)
            observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(RETRY_COUNT.toLong())//请求失败重连次数
                .subscribe(this)
        }


    }

  private  fun getDisMap(): MutableMap<Any, DisposableObserver<*>> ?{
        if (disMap == null) {
            disMap = LinkedHashMap()
        }
        return disMap as  MutableMap<Any, DisposableObserver<*>>
    }

    /**
     * 获取注解服务
     * @return
     */
    fun getApi(): HttpApi {
        if (httpApi == null) {
            httpApi = createApiService(HttpApi::class.java)
        }
        return httpApi as HttpApi
    }

    /**
     * 取消全部请求
     */
     fun disposeAllRequest() {
        val set = disMap?.keys
        if (set != null) {
            for (obj in set) {
                disposeRequest(obj)
            }
        }
        getDisMap()?.clear()
        disMap = null
    }

    /***
     * 取消单个请求
     * @param position
     */
    fun disposeRequest(position: Any) {
        (getDisMap()?.get(position) as  DisposableObserver<*>) .dispose()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        disposeAllRequest()
    }

}





