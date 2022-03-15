package com.rw.personalcenter.presenter

import androidx.lifecycle.LifecycleOwner
import com.rw.basemvp.presenter.BaseView
import com.rw.basemvp.presenter.MvpPresenter

/**
 * Created by Amuse
 * Date:2021/8/24.
 * Desc:
 */
class PersonalCenterPresenter :MvpPresenter<BaseView>(){
    override fun getBaseUrl(): String {
        return "http://192.168.1.5:8080"
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