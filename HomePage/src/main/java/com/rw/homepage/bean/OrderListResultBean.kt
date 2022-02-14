package com.rw.homepage.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2022/2/14.
 * Desc:
 */

data class OrderListResultBean (
    var data: OrderDataBean?
) : BaseBean()

data class OrderDataBean(
    var data: MutableList<OrderItemBean>?,
    val count:Int,
    val pageSize:Int,
    val pageNum:Int
)

data class OrderItemBean(
    val goodGroup:String,
    val orderTime:String,
    val goodsPrice:String,
    val customerId:Int,
    val id:Int,
    val customerAccount:String,
    val orderState:Int,
    val goodsList:List<GoodsListItemBean>?

)
data class GoodsListItemBean(
    val goodsId:Int,
    val categoryId:Int,
    val goodsName:String,
    val categoryName:String,
    val num:Int,
    val normsAttributeList:List<GoodsAttributeItemBean>?
)

data class GoodsAttributeItemBean(
    val id:Int,
    val normsAttributeName:String,
    val normsId:Int,
    val attributeTime:String
)



