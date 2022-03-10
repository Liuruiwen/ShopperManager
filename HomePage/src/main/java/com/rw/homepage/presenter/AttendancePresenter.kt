package com.rw.homepage.presenter

import com.rw.homepage.HttpApi
import com.rw.homepage.bean.AttendanceResultBean
import com.rw.homepage.bean.OrderListResultBean
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2022/3/10.
 * Desc:
 */
class AttendancePresenter :HomePagePresenter(){

    /**
     * 获取规格属性列表
     */
    fun getAttendance(req: AttendanceReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_ATTENDANCE, AttendanceResultBean::class.java, true,
                mapOf("token" to bean.token),req
            )
        }

    }


    data class AttendanceReq(
        var time:String
    )
}