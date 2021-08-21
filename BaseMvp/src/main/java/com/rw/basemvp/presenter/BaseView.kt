package com.rw.basemvp.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.bean.BaseModel


interface BaseView {

    /**
     * 显示loading对话框
     *
     * @param requestType 接口类型
     * @param result 解析成功后返回vo对象
     */
//     fun<T :BaseBean> onShowResult(requestType: Int?=0, result: T)

    /**
     * 数据加载成功后的回调
     */
     fun onBeforeSuccess()

    /**
     * 显示loading对话框
     */
     fun onShowLoading()

    /**
     * 隐藏loading对话框
     */
     fun onHideLoading()

    /**
     * 显示错误信息
     *
     * @param errorMsg
     */
//     fun onShowError(errorMsg: String?="", errorType: Int?=-1,position:Int?=-1)

     fun getViewModel(): BaseModel?


}