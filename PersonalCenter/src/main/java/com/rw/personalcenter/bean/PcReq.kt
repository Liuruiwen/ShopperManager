package com.rw.personalcenter.bean

/**
 * Created by Amuse
 * Date:2022/3/17.
 * Desc:
 */

/**
 * 同意员工缺勤传参
 */
data class AgreeRequestCardReq (
    var account:String,
    var id:Int,
    var absenteeismTime:String
)


/**
 * 删除员工缺勤传参
 */
data class DeleteRequestCardReq (
    var id:Int
)