package com.rw.service

import com.rw.service.bean.AccountBean
import com.rw.service.inter.IAccountService

/**
 * Created by Amuse
 * Date:2021/4/4.
 * Desc:
 */
class EmptyAccountService: IAccountService {
    override fun isLogin(): Boolean {
        return false
    }

    override fun  getLoginBean(): AccountBean? {
        return null
    }

}