package com.rw.personalcenter.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2021/9/1.
 * Desc:
 */
data class EmployeesManagerBean(
    var data: MutableList<EmployeesBean>?
) : BaseBean()

data class EmployeesBean(
    val id:Int,
    val userName:String?,
    val age:Int,
    val sex:Int,
    val address:String?,
    val level:Int,
    val account:String,
    val nickName:String?,
    val businessLicense:String

)

