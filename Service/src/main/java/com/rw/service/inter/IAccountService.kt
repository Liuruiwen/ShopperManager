package com.rw.service.inter

import com.rw.service.bean.AccountBean

/**
 * Created by Amuse
 * Date:2021/4/4.
 * Desc:
 */
interface IAccountService {
    fun isLogin(): Boolean
    fun getLoginBean(): AccountBean?
}