package com.rw.basemvp.news

import android.util.Log
import com.google.gson.Gson
import com.rw.basemvp.bean.ErrorBean
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException


/**
 * 请求结果处理
 */
fun <T> Observable<T>.subscribeResult(next:((t:T?)->Unit)?=null, ex:((error: ErrorBean)->Unit)?=null,complete:(()->Unit)?=null): DisposableObserver<T> {

   return  this.subscribeWith(object : DisposableObserver<T>() {
        override fun onComplete() {
            if (complete!=null)complete()
        }

        override fun onNext(t: T) {
          if (next!=null) next(t)
        }

        override fun onError(e: Throwable) {
           if (ex!=null){
               try {
                   if (e is SocketTimeoutException) {//请求超时
                       ex(ErrorBean(456,"请求超时", ""))
                   } else if (e is ConnectException) {//网络连接超时
                       //                ToastManager.showShortToast("网络连接超时");
                       ex(ErrorBean(456,"服务器地址不正确", ""))
                   } else if (e is SSLHandshakeException) {//安全证书异常
                       //                ToastManager.showShortToast("安全证书异常");
                       ex(ErrorBean(556,"安全证书异常", ""))
                   } else if (e is HttpException) {//请求的地址不存在
                       val code = e.code()
                       if (code == 504) {
                           //                    ToastManager.showShortToast("网络异常，请检查您的网络状态");
                           ex(ErrorBean(504,"网络异常，请检查您的网络状态", e.response()?.raw()?.request()?.url()?.url()?.path))
//                    baseView?.onShowError("网络异常，请检查您的网络状态", position)
                       } else if (code == 404) {
                           //                    ToastManager.showShortToast("请求的地址不存在");
                           ex(ErrorBean(404,"请求的地址不存在", e.response()?.raw()?.request()?.url()?.url()?.path))
                       } else {
                           //                    ToastManager.showShortToast("请求失败");
                           ex(ErrorBean(404,"请求失败", e.response()?.raw()?.request()?.url()?.url()?.path))
                       }
                   } else if (e is UnknownHostException) {//域名解析失败
                       //                ToastManager.showShortToast("域名解析失败");
                       ex(ErrorBean(110,"域名解析失败", ""))
                   } else {
                       //                ToastManager.showShortToast("error:" + e.getMessage());
                       ex(ErrorBean(110, e.message,""))
                   }
               } catch (e2: Exception) {
                   e2.printStackTrace()
               } finally {
                   ex(ErrorBean(456,e.message, ""))
                   Log.e("OnSuccessAndFaultSub", "error:" + e.message)
               }
           }

        }

    })

}

/**
 * 线程池处理
 */
fun<T> Observable<T>.threadIo(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}

/**
 * 传参处理
 */
fun<T> RequestBody.bodyCreate(bean:Class<T>): RequestBody {
   return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(bean))

}