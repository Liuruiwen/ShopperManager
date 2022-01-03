package com.rw.map.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.presenter.DefaultPresenter
import com.rw.map.HttpApi
import com.rw.map.bean.MapReq
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2021/12/30.
 * Desc:
 */
class MapPresenter: DefaultPresenter() {
    override fun getBaseUrl(): String {
        return "http://192.168.1.3:8080"
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

}