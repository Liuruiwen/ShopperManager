package com.rw.basemvp.http

import android.util.Log
import com.google.gson.Gson
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.bean.ErrorBean
import com.rw.basemvp.presenter.BaseView
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/*   
* @Author:      Amuser
* @CreateDate:   2019-12-19 13:49
*@Description: 
*/
class RxRequestResult<T : BaseBean, V : BaseView?> constructor(p: Int, bean: Class<T>, view: V) :
    DisposableObserver<Response<ResponseBody>>() {

    var position: Int? = 0
    var baseBean: Class<T>? = null
    var baseView: V? = null

    init {
        this.position = p
        this.baseBean = bean
        this.baseView = view
    }

    override fun onComplete() {
        baseView?.onHideLoading()
    }

    override fun onNext(response: Response<ResponseBody>) {
        baseView?.onHideLoading()
        if (response.isSuccessful) {
            try {
                val result=response.body()?.string()
                val bean = Gson().fromJson(result, baseBean)
//                when(bean.errorCode){
//                    0-> baseView?.onShowResult(position,bean)
//                    else -> baseView?.onShowError(bean.errorMsg,bean.errorCode,position)
//                }

                when(bean.code){
                    200->{
                        bean.requestCount=position
                        bean.requestType=response.raw().request().url().url().path
                        baseView?.getViewModel()?.baseBean?.postValue(bean)


//                        val beans=BaseBean()
//                        beans.result=result
//                        if (baseView?.getViewModel()?.baseBean==null){
//                                 val ooo=""
//                        }else{
//                            if (beans!=null){
//                                baseView?.getViewModel()?.baseBean?.value=beans
//                                if ( baseView?.getViewModel()?.baseBean?.value!=null){
//                                    baseView?.onShowResult(position,bean)
//                                }else{
//                                    val cc=""
//                                }
//                            }
//
//                        }



                    }
                    else ->{
//                        baseView?.onShowError("请求异常",1001,position)
                        baseView?.getViewModel()?.errorBean?.value=ErrorBean(bean.code,bean.message,
                                response.raw().request().url().url().path)
                    }
                }

            } catch (e: Exception) {
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(303,"数据解析异常",
                        response.raw().request().url().url().path)
                e.printStackTrace()
            }
        }
    }

    override fun onError(e: Throwable) {
        try {
            baseView?.onHideLoading()
            if (e is SocketTimeoutException) {//请求超时
            } else if (e is ConnectException) {//网络连接超时
                //                ToastManager.showShortToast("网络连接超时");
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(456,"服务器地址不正确",
                        "")
            } else if (e is SSLHandshakeException) {//安全证书异常
                //                ToastManager.showShortToast("安全证书异常");
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(556,"安全证书异常",
                        "")
            } else if (e is HttpException) {//请求的地址不存在
                val code = e.code()
                if (code == 504) {
                    //                    ToastManager.showShortToast("网络异常，请检查您的网络状态");
                    baseView?.getViewModel()?.errorBean?.value=ErrorBean(504,"网络异常，请检查您的网络状态",
                            e.response()?.raw()?.request()?.url()?.url()?.path)
//                    baseView?.onShowError("网络异常，请检查您的网络状态", position)
                } else if (code == 404) {
                    //                    ToastManager.showShortToast("请求的地址不存在");
                    baseView?.getViewModel()?.errorBean?.value=ErrorBean(404,"请求的地址不存在",
                            e.response()?.raw()?.request()?.url()?.url()?.path)
                } else {
                    //                    ToastManager.showShortToast("请求失败");
                    baseView?.getViewModel()?.errorBean?.value=ErrorBean(404,"请求失败",
                            e.response()?.raw()?.request()?.url()?.url()?.path)
                }
            } else if (e is UnknownHostException) {//域名解析失败
                //                ToastManager.showShortToast("域名解析失败");
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(110,"域名解析失败",
                       "")
            } else {
                //                ToastManager.showShortToast("error:" + e.getMessage());
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(110, e.message,"")
            }
        } catch (e2: Exception) {
            e2.printStackTrace()
        } finally {
            Log.e("OnSuccessAndFaultSub", "error:" + e.message)
            //            mOnSuccessAndFaultListener.onFault("error:" + e.getMessage());
            //            dismissProgressDialog();
        }
    }
}