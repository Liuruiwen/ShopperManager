package com.rw.homepage.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2022/3/10.
 * Desc:
 */
data class AttendanceResultBean (
    var data: MutableList<AttendanceBean>?
) : BaseBean()


data class AttendanceBean(
    val id:Int,
    val clockTime:String?,
    val afterWorkTime:String?,
    val clockDate:String,
    var clockType:Int,
    var days: String
)