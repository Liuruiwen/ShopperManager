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
    val categoryId:Int,
    val goodsDesc:String,
    val goodsPrice:String,
    val goodsRanking:Int,
    val shelvesType:Int,
    val monthSales:Int,
    val annualSales:Int,
    val goodsPosition:Int,
    val listNorms:List<NormsHeaderBean>,
    var isShow:Boolean
)
//private int id;//规格id
//private String normsName;//规格名称
//private String businessLicense;//商家唯一标识码
//private int categoryId;//品类id
data class AttributeBean(
   val id:Int,
   val normsName:String?,
   val businessLicense:String,
   val listAttribute:List<ListAttributeBean>?
)

data class ListAttributeBean(
    val id:Int,
    val normsAttributeName:String,
    val normsId:Int
)

data class NormsId(
    var normsId:Int,
    var list:ArrayList<Int>
)