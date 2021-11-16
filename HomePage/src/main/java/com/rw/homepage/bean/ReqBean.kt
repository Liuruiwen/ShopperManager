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
   val id:Int
)
//"id": 2,
//"categoryName": "霸王奶茶",
//"categoryDesc": "你想喝不，交钱"

/**
 * 编辑商品品类
 */
data class EditCategoryReq(
  val id:Int,
  val categoryName:String?,
  val categoryDesc:String?
)