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
    const val HTTP_EDIT_GOODS="/s1/order/editOrderGoods"//编辑商品
    const val HTTP_DELETE_GOODS="/s1/order/deleteGoods"//删除商品
    const val HTTP_ADD_NORMS="/s1/order/insertNorms"//新增商品规格
    const val HTTP_ADD_ATTRIBUTE="/s1/order/insertNormsAttribute"//新增规格属性
    const val HTTP_DELETE_NORMS="/s1/order/deleteNorms"//删除商品规格
    const val HTTP_DELETE_ATTRIBUTE="/s1/order/deleteNormsAttribute"//删除规格属性
    const val HTTP_GET_NORMS_LIST="/s1/order/getNormsList"//查询规格列表
    const val HTTP_GET_SHOPPER_DETAIL="/s1/order/getShopperDetail"//获取店铺详情
    const val HTTP_GET_ORDER_LIST="/s1/order/getOrderList"//获得订单列表
    const val HTTP_GET_ATTENDANCE="/s1/index/getEmployeesAttendance"//获取员工考勤
    const val HTTP_COMMIT_CARD="/s1/index/commitCard"//获取员工考勤
    const val HTTP_UPLOAD_IMAGE="/s1/index/uploadAvatar"//图片上传
    const val HTTP_UPDATE_POSITION="/s1/index/updateCategoryPosition"//更改商品序号

}