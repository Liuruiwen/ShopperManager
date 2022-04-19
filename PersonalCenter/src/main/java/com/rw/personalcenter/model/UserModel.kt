package com.rw.personalcenter.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rw.personalcenter.bean.EditUserBean

/**
 * Created by Amuse
 * Date:2022/4/19.
 * Desc:
 */
open class UserModel: ViewModel() {
    companion object {
        private var instance: UserModel? = null
            get() {
                if (field == null) {
                    field = UserModel()
                }
                return field
            }

        fun get(): UserModel?{
            return instance
        }
    }
    var userInfo= MutableLiveData<EditUserBean>()

}