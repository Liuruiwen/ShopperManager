package com.rw.homepage

/**
 * Created by Amuse
 * Date:2021/9/13.
 * Desc:
 */
object HttpApi {
    const val HTTP_GET_CATEGORY_LIST="/s1/index/selectCategory"//获取品类列表
    const val HTTP_ADD_CATEGORY="/s1/index/insertCategory"//添加品类
    const val HTTP_DELETE_CATEGORY="/s1/index/deleteCategory"//删除品类
    const val HTTP_GET_GOODS_LIST="/s1/order/getCategoryGoodList"//获取商品列表
    const val HTTP_EDIT_CATEGORY="/s1/order/editCategory"//编辑品类
    const val HTTP_ADD_GOODS="/s1/order/insertOrderGoods"//添加商品
    const val HTTP_DELETE_GOODS="/s1/order/deleteGoods"//删除商品
    const val HTTP_ADD_NORMS="/s1/order/insertNorms"//新增商品规格
    const val HTTP_ADD_ATTRIBUTE="/s1/order/insertNormsAttribute"//新增规格属性
    const val HTTP_DELETE_NORMS=""//删除商品规格
    const val HTTP_DELETE_ATTRIBUTE="/s1/order/deleteNormsAttribute"//删除规格属性
    const val HTTP_GET_NORMS_LIST="/s1/order/getNormsList"//查询规格列表

}