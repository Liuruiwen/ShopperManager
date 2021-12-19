package com.rw.homepage.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rw.homepage.bean.MultiItemBean

/**
 * Created by Amuse
 * Date:2021/12/19.
 * Desc:
 */
class GoodsEditModel :ViewModel(){
    companion object{
        private var instance:GoodsEditModel?=null
            get() {
                if (field==null){
                    field=GoodsEditModel()
                }

                return  field
            }

        fun get(): GoodsEditModel?{
            return instance
        }

        fun destroy(){
            instance=null
        }
    }


    val normsList=MutableLiveData<MutableList<MultiItemBean>>()

}