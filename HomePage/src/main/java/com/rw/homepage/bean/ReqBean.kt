package com.rw.homepage.bean

/**
 * Created by Amuse
 * Date:2021/9/14.
 * Desc:请求bean
 */

data class ReqAddCategory(
   val  categoryName:String,
   val  categoryDesc:String,
   val  categoryPosition:Int
)
data class ReqGoodsList(
   val id:Int
)