package com.rw.homepage.bean

/**
 * Created by Amuse
 * Date:2021/9/14.
 * Desc:请求bean
 */

data class AddCategoryReq(
   val  categoryName:String,
   val  categoryDesc:String
)
data class GoodsListReq(
   val categoryId:Int
)


/**
 * 编辑商品品类
 */
data class EditCategoryReq(
  val id:Int,
  val categoryName:String?,
  val categoryDesc:String?
)



/*
  添加商品
 */
data class AddGoodsReq(
    val categoryId:Int,
    val goodsPrice:String,
    val goodsName:String,
    val goodsDesc:String,
    val goodsImage:String,
    val normsList:String,//规格列表json字符串
    val shelvesType:Int//是否上下架

)

/**
 * 删除商品
 */
data class DeleteGoodsReq(
    val id:Int
)

/**
 * 获取规格列表Req
 */
data class NormsListReq(
    val categoryId:String
)

/**
 * 添加规格属性
 */
data class AddNormsAttribute(
    val normsAttributeName:String,
    val normsId:String
)

/**
 * 添加规格属性
 */
data class AddNormsReq(
    val normsName:String,
    val categoryId:String
)


/**
 * 删除规格
 */
data class DeleteNormsReq(
    val id:Int
)