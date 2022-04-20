package com.rw.homepage.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.homepage.HttpApi
import com.rw.homepage.bean.AddGoodsReq
import com.rw.homepage.bean.GoodsBean
import com.rw.homepage.bean.GoodsListReq
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2021/10/19.
 * Desc:
 */
class GoodsEditPresenter : HomePagePresenter() {
    /**
     * 添加商品
     */
    fun reqAddGoods(req:AddGoodsReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_ADD_GOODS, BaseBean::class.java, true,
                linkedMapOf("token" to bean.token), req
            )
        }

    }
}