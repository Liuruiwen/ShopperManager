package com.rw.homepage.presenter

import com.rw.homepage.HttpApi
import com.rw.homepage.bean.GoodsBean
import com.rw.homepage.bean.ReqGoodsList
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2021/9/15.
 * Desc:
 */
class GoodsListPresenter : HomePagePresenter(){
    /**
     * 获取品类信息
     */
    fun reqGoodsList(id:Int) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_GOODS_LIST, GoodsBean::class.java, true,
                mapOf("token" to bean.token), ReqGoodsList(id)
            )
        }

    }

}