package com.rw.homepage.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2022/1/12.
 * Desc:
 */
data class ShopperResultBean (
    var data: ShopperDataBean?
) : BaseBean()

data class ShopperDataBean(
    val todayMoney:String?,
    val yesterdayMoney:String?,
    val monthMoney:String?,
    val address:ShopperAddressBean
)

data class ShopperAddressBean(
    val id:Int,
    val shopAddress:String,
    val longitude:String,
    val latitude:String
)