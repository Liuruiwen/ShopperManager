package com.rw.personalcenter.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2022/3/16.
 * Desc:
 */
data class RequestCardBean (
    var data: MutableList<RequestCardItem>?
) : BaseBean()



data class RequestCardItem(
    val id:Int,
    val cardTime:String,
    val absenteeismTime:String,
    val account:String,
    val businessLicense:String,
    var cardState:Int
)