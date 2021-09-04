package com.rw.basemvp.until

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Amuse
 * Date:2021/9/4.
 * Desc:
 */
class SpUtil() {
    private var mSharePf: SharedPreferences?=null
    private val shared_preferences="app_sp"
    companion object {
        private var instance: SpUtil? = null
            get() {
                if (field == null) {
                    field = SpUtil()
                }
                return field
            }

        fun get(): SpUtil?{
            return instance
        }
    }
   fun getSp(context: Context,main:String):SharedPreferences.Editor?{

       return getSharedPreferences(context,main)?.edit()
   }

    private fun getSharedPreferences(context: Context?,main:String):SharedPreferences?{
        if (context==null)return null
        if (mSharePf==null){
            mSharePf=context.applicationContext.getSharedPreferences(main,Context.MODE_PRIVATE)
        }

        return mSharePf
    }

    private fun getSp(context: Context):SharedPreferences.Editor?{
        return getSp(context,shared_preferences)
    }

    fun spString(context: Context,value:String,key:String){
        getSp(context)?.putString(key,value)?.commit()
    }

    fun spBoolean(context: Context,value:Boolean,key:String){
        getSp(context)?.putBoolean(key,value)?.commit()
    }

    fun spInt(context: Context,value:Int,key:String){
        getSp(context)?.putInt(key,value)?.commit()
    }

    fun spStringSet(context: Context,value:Set<String>,key:String){
        getSp(context)?.putStringSet(key,value)?.commit()
    }

    fun remove(context: Context,key: String){
        getSp(context)?.remove(key)
    }

    fun clearAll(context: Context){
        getSp(context)?.clear()
    }


    fun spGetString(context: Context,key:String):String?{
        return  getSharedPreferences(context,shared_preferences)?.getString(key,"")
    }

    fun spGetBoolean(context: Context,key:String):Boolean?{
      return  getSharedPreferences(context,shared_preferences)?.getBoolean(key,false)
    }

    fun spGetInt(context: Context,key:String):Int?{
        return  getSharedPreferences(context,shared_preferences)?.getInt(key,0)
    }






}