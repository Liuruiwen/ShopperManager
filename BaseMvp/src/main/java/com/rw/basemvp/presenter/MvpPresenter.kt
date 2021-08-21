package com.rw.basemvp.presenter

import com.google.gson.Gson
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.http.RxRequestResult
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

/*   
* @Author:      Amuser
* @CreateDate:   2019-12-18 14:55
*@Description: 
*/
abstract class MvpPresenter<V :BaseView> :RetrofitPresenter<V>(){


    /**
     * get请求
     * @param position 请求序列号
     * @param url 地址
     * @param bean 请求实体类
     * @param isShow 是否需要弹窗
     * @param <T>
    </T> */
    fun <T : BaseBean> getData(position: Int, url: String, bean: Class<T>, isShow: Boolean) {
        if (isShow) {
            getView()?.onShowLoading()
        }
        toSubscribe(getApi().rxGet(url), RxRequestResult(position, bean, getView()), position)
    }

    /**
     * get请求
     * @param position 请求序列号
     * @param url 地址
     * @param bean 请求实体类
     * @param isShow 是否需要弹窗
     * @param <T>
    </T> */
    fun <T : BaseBean> getData(position: Int, url: String, bean: Class<T>, isShow: Boolean,queryMap: Map<String, Any>) {
        if (isShow) {
            getView()?.onShowLoading()
        }
        toSubscribe(getApi().rxGet(url,queryMap), RxRequestResult(position, bean, getView()), position)
    }

    /**
     * post请求
     * @param position 请求序列号
     * @param url 地址
     * @param bean 请求实体类
     * @param isShow 是否需要弹窗
     * @param <T>
    </T> */
    fun <T : BaseBean> postData(position: Int, url: String, bean: Class<T>, isShow: Boolean,queryMap: Map<String, Any>) {
        if (isShow) {
            getView()?.onShowLoading()
        }
        toSubscribe(getApi().rxPost(url,queryMap), RxRequestResult(position, bean, getView()), position)
    }

    /**
     * post请求
     * @param position 请求序列号
     * @param url 地址
     * @param bean 请求实体类
     * @param isShow 是否需要弹窗
     * @param <T>
    </T> */
    fun <T : BaseBean> postBodyData(position: Int, url: String, bean: Class<T>, isShow: Boolean, objects: Any) {
        if (isShow) {
            getView()?.onShowLoading()
        }
        toSubscribe(getApi().rxBodyPost(url,getRequestBody(objects)), RxRequestResult(position, bean, getView()), position)
    }


    fun getRequestBody(objects: Any):RequestBody{
       return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),Gson().toJson(objects))
    }


}