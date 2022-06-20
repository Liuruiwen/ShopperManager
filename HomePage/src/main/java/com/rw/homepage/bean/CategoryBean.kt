package com.rw.homepage.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2021/9/13.
 * Desc:
 */
/**
 * 获取品类列表
 */
data class CategoryListBean (
    var data: MutableList<CategoryResultBean>?
) : BaseBean()

/**
 * 增加品类
 */
data class CategoryBean (
    var data: CategoryResultBean
) : BaseBean()

/**
 * 增加品类
 */
data class EditCategoryResultBean (
    var data: EditCategoryReq?
) : BaseBean()

data class CategoryResultBean(
    val id:Int,
    var categoryName:String?,
    var categoryPosition:Int?,
    var categoryDesc:String?,
    val businessLicense:String?
)