package com.rw.map.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rw.map.bean.MapReq

/**
 * Created by Amuse
 * Date:2022/1/12.
 * Desc:
 */
open class MapModel: ViewModel() {
    companion object{
        private var instance:MapModel?=null
            get() {
                if (field==null){
                    field=MapModel()
                }

                return  field
            }

        fun get(): MapModel?{
            return instance
        }

        fun destroy(){
            instance=null
        }
    }
  val address=MutableLiveData<MapReq>()
}