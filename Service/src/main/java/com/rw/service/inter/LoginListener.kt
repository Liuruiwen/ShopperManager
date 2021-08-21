package com.rw.service.inter

/**
 * Created by Amuse
 * Date:2021/4/4.
 * Desc:
 */
interface LoginListener:ILoginStateListener {
    override fun login() {
    }

    override fun loginOut() {
    }
}