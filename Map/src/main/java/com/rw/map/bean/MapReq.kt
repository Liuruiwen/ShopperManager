package com.rw.map.bean

/**
 * Created by Amuse
 * Date:2022/1/3.
 * Desc:
 */
data class MapReq(
    val shopAddress:String,
    val longitude:String,
    val latitude:String,
    val type:Int,//1、是增加，2、是修改
    val id:Int=0
)

/**
 * 查询打卡状态
 */
data class ClockStateReq(
    val clockTime:String
)

/**
 * 打卡
 */
data class ClockWorkReq(
    val clockTime:String,
    val clockAddress:String,
    val clockState:String
)