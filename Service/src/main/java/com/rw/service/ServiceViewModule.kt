package com.rw.service

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rw.service.bean.AccountBean
import com.rw.service.bean.LoginOutBean

/**
 * Created by Amuse
 * Date:2021/4/18.
 * Desc:
 */
open class ServiceViewModule:ViewModel() {
    companion object {
        private var instance: ServiceViewModule? = null
            get() {
                if (field == null) {
                    field = ServiceViewModule()
                }
                return field
            }

        fun get(): ServiceViewModule?{
            return instance
        }
    }
    var loginService=MutableLiveData<AccountBean>()

    var loginOutService=MutableLiveData<LoginOutBean>()
}