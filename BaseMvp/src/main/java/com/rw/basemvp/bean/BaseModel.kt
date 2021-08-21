package com.rw.basemvp.bean

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * Created by Amuse
 * Date:2021/3/29.
 * Desc:
 */
class BaseModel(application: Application) :AndroidViewModel(application) {
   var baseBean=MutableLiveData<BaseBean>()
   var errorBean=MutableLiveData<ErrorBean>()
}