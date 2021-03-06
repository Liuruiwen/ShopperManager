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
//                        baseView?.onShowError("????????????",1001,position)
                        baseView?.getViewModel()?.errorBean?.value=ErrorBean(bean.code,bean.message,
                                response.raw().request().url().url().path)
                    }
                }

            } catch (e: Exception) {
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(303,"??????????????????",
                        response.raw().request().url().url().path)
                e.printStackTrace()
            }
        }
    }

    override fun onError(e: Throwable) {
        try {
            baseView?.onHideLoading()
            if (e is SocketTimeoutException) {//????????????
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(456,"????????????",
                    "")
            } else if (e is ConnectException) {//??????????????????
                //                ToastManager.showShortToast("??????????????????");
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(456,"????????????????????????",
                        "")
            } else if (e is SSLHandshakeException) {//??????????????????
                //                ToastManager.showShortToast("??????????????????");
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(556,"??????????????????",
                        "")
            } else if (e is HttpException) {//????????????????????????
                val code = e.code()
                if (code == 504) {
                    //                    ToastManager.showShortToast("??????????????????????????????????????????");
                    baseView?.getViewModel()?.errorBean?.value=ErrorBean(504,"??????????????????????????????????????????",
                            e.response()?.raw()?.request()?.url()?.url()?.path)
//                    baseView?.onShowError("??????????????????????????????????????????", position)
                } else if (code == 404) {
                    //                    ToastManager.showShortToast("????????????????????????");
                    baseView?.getViewModel()?.errorBean?.value=ErrorBean(404,"????????????????????????",
                            e.response()?.raw()?.request()?.url()?.url()?.path)
                } else {
                    //                    ToastManager.showShortToast("????????????");
                    baseView?.getViewModel()?.errorBean?.value=ErrorBean(404,"????????????",
                            e.response()?.raw()?.request()?.url()?.url()?.path)
                }
            } else if (e is UnknownHostException) {//??????????????????
                //                ToastManager.showShortToast("??????????????????");
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(110,"??????????????????",
                       "")
            } else {
                //                ToastManager.showShortToast("error:" + e.getMessage());
                baseView?.getViewModel()?.errorBean?.value=ErrorBean(110, e.message,"")
            }
        } catch (e2: Exception) {
            e2.printStackTrace()
        } finally {
            baseView?.getViewModel()?.errorBean?.value=ErrorBean(456,e.message,
                "")
            Log.e("OnSuccessAndFaultSub", "error:" + e.message)
            //            mOnSuccessAndFaultListener.onFault("error:" + e.getMessage());
            //            dismissProgressDialog();
        }
    }
}