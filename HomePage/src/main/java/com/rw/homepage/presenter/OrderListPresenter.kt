package com.rw.homepage.presenter

import com.rw.homepage.HttpApi
import com.rw.homepage.bean.OrderListResultBean
import com.rw.homepage.bean.ShopperResultBean
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2022/2/9.
 * Desc:
 */
class OrderListPresenter:HomePagePresenter() {

    /**
     * 获取规格属性列表
     */
    fun getOrderList(req:OrderListReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_ORDER_LIST, OrderListResultBean::class.java, false,
                mapOf("token" to bean.token),req
            )
        }

    }

    data class OrderListReq(
        val pageNum:Int,
        val pageSize:Int
    )
}