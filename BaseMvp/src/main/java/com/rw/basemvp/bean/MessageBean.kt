package com.rw.basemvp.bean

/**
 * Created by Amuse
 * Date:2021/10/8.
 * Desc:
 */
data class MessageBean<T> (
    var code:Int?,
    var message:String?,
    var requestCount:Int?,
    var requestType:String?,
    var data:T
)