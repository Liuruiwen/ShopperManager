package com.rw.personalcenter.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2021/8/26.
 * Desc:
 */
data class UserInfoBean(
    var data: UserBean
) : BaseBean()

data class UserBean(
    val userName: String?,
    val age: Int?,
    val account: String,
    val userId: Int,
    val  employees: Employees?,
    val shopAddress : ShopAddress?,
    val address:String?,
    val  headerUrl:String?
    )

data class Employees(
    val level: Int,
    val nickName: String,
    val content: String,
    val employeesType: Int

)

data class ShopAddress(
    val id:Int,
    val shopAddress:String,
    val longitude:String,
    val latitude:String
)