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