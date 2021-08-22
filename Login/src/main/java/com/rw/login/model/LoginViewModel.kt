package com.rw.login.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Amuse
 * Date:2021/8/22.
 * Desc:
 */
class LoginViewModel : ViewModel() {
    companion object{
        private var instance:LoginViewModel?=null
            get() {
            if (field==null){
                field=LoginViewModel()
            }

            return  field
        }

        fun get(): LoginViewModel?{
            return instance
        }

        fun destroy(){
            instance=null
        }
    }

    var isFinish=MutableLiveData<Int>()
}