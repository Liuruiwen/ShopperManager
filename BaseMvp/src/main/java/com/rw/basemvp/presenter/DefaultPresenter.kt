package com.rw.basemvp.presenter

import androidx.lifecycle.LifecycleOwner

/**
 * Created by Amuse
 * Date:2021/4/15.
 * Desc:
 */
open class DefaultPresenter:MvpPresenter<BaseView> (){
    override fun getBaseUrl(): String {
        return ""
    }

    override fun getToken(): String {
        return ""
    }

    override fun onCreate(owner: LifecycleOwner) {
    }

    override fun onStart(owner: LifecycleOwner) {
    }

    override fun onStop(owner: LifecycleOwner) {
    }

    override fun onResume(owner: LifecycleOwner) {
    }

    override fun onPause(owner: LifecycleOwner) {
    }
}