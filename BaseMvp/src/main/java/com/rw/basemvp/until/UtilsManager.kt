package com.rw.basemvp.until

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by Amuse
 * Date:2020/3/30.
 * Desc:
 */
class UtilsManager {
    var mContext:Context?=null
    constructor(  context:Context){
        mContext=context;
    }
    companion object{
        private var instance:UtilsManager?=null
        fun getInstance(context:Context):UtilsManager{
            if(instance==null){
                synchronized(UtilsManager::class) {
                    instance = UtilsManager(context.applicationContext)
                }
            }

            return instance!!
        }
    }

    /**
     * 获取手机屏幕的宽度
     *
     * @return
     */
    fun getWindowWidth(): Int {
        return  mContext?.getResources()?.getDisplayMetrics()!!.widthPixels
    }
}