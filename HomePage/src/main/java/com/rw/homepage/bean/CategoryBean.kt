package com.rw.homepage.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2021/9/13.
 * Desc:
 */
data class CategoryBean (
    var data: MutableList<CategoryResultBean>?
) : BaseBean()

data class CategoryResultBean(
    val id:Int,
    val categoryName:String?,
    val categoryPosition:Int?,
    val categoryDesc:String?,
    val businessLicense:String?
)