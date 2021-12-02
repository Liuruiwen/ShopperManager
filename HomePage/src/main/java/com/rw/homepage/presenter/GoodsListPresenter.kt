package com.rw.homepage.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.homepage.HttpApi
import com.rw.homepage.bean.DeleteGoodsReq
import com.rw.homepage.bean.GoodsBean
import com.rw.homepage.bean.GoodsListReq
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2021/9/15.
 * Desc:
 */
class GoodsListPresenter : HomePagePresenter(){
    /**
     * 获取商品列表
     */
    fun reqGoodsList(id:Int) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_GOODS_LIST, GoodsBean::class.java, true,
                mapOf("token" to bean.token), GoodsListReq(id)
            )
        }

    }

    /**
     * 获取商品列表
     */
    fun reqDeleteGoods(id:Int) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_DELETE_GOODS, BaseBean::class.java, true,
                mapOf("token" to bean.token), DeleteGoodsReq(id)
            )
        }

    }

}