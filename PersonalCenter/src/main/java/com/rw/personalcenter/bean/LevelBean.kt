package com.rw.personalcenter.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2021/9/2.
 * Desc:
 */
data class LevelDataBean (
    var data: MutableList<LevelBean>?
) : BaseBean()


data class LevelBean(
    val employeesId:Int,
    val level:Int,
    val nickName:String,
    val content:String,
    val employeesType:Int
)