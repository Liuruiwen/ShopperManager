package com.rw.personalcenter.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.bean.AgreeRequestCardReq
import com.rw.personalcenter.bean.DeleteRequestCardReq
import com.rw.personalcenter.bean.RequestCardBean
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2022/3/16.
 * Desc:
 */
class RequestCardPresenter :PersonalCenterPresenter(){
    /**
     * 获取员工缺勤列表
     */
    fun getRequestCard() {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_REQUEST_CARD, RequestCardBean::class.java, true,
                linkedMapOf("token" to bean.token)
            )
        }

    }

    /**
     * 同意员工考勤
     */
    fun agreeRequestCard(req: AgreeRequestCardReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_AGREE_REQUEST_CARD, BaseBean::class.java, true,
                linkedMapOf("token" to bean.token),req
            )
        }

    }
    /**
     * 删除员工考勤
     */
    fun deleteRequestCard(req: DeleteRequestCardReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_DELETE_REQUEST_CARD, BaseBean::class.java, true,
                linkedMapOf("token" to bean.token),req
            )
        }

    }
}