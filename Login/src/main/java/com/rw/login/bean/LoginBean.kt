package com.rw.login.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2021/4/14.
 * Desc:
 */
data class LoginBean (
        var data: DataBean
):BaseBean()


data class DataBean(
       val token:String,
       val userName:String,
       val account:String,
       val userId:String
)
