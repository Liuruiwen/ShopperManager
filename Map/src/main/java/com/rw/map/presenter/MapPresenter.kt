package com.rw.map.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.presenter.DefaultPresenter
import com.rw.map.HttpApi
import com.rw.map.bean.ClockStateReq
import com.rw.map.bean.ClockStateResultBean
import com.rw.map.bean.ClockWorkReq
import com.rw.map.bean.MapReq
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2021/12/30.
 * Desc:
 */
class MapPresenter: DefaultPresenter() {
    override fun getBaseUrl(): String {
        return "http://192.168.1.5:8080"
    }
    /**
     * 添加地址和修改地址
     */
    fun addOrEditAddress(req: MapReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                1,
                HttpApi.HTTP_ADD_ADDRESS, BaseBean::class.java, true,
                mapOf("token" to bean.token),req
            )
        }

    }

    /**
     * 获取打卡状态
     */
    fun getClockState(req: ClockStateReq){
        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                1,
                HttpApi.HTTP_CLOCK_STATE, ClockStateResultBean::class.java, true,
                mapOf("token" to bean.token),req
            )
        }
    }

    /**
     * 打卡
     */
    fun clockWork(req: ClockWorkReq){
        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                1,
                HttpApi.HTTP_CLOCK_WORK, BaseBean::class.java, true,
                mapOf("token" to bean.token),req
            )
        }
    }

}