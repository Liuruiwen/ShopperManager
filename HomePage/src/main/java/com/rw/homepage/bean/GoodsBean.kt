package com.rw.homepage.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2021/9/15.
 * Desc:
 */
data class GoodsBean  (
    var data: MutableList<GoodsListBean>?
) : BaseBean()


data class GoodsListBean(
    val id:Int,
    val goodsName:String,
    val goodsImage:String,
    val goodsCategory:Int,
    val goodsDesc:String,
    val goodsPrice:String,
    val goodsRanking:Int,
    val shelvesType:Int,
    val list:List<AttributeBean>,
    var isShow:Boolean
)

data class AttributeBean(
   val type:Int,
   val attributeName:String,
   val listAttribute:List<ListAttributeBean>?
)
data class ListAttributeBean(
    val id:Int,
    val name:String
)