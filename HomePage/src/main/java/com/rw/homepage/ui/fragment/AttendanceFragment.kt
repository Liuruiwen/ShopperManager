package com.rw.homepage.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rw.basemvp.BaseFragment
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.AttendanceAdapter
import com.rw.homepage.bean.AttendanceBean
import com.rw.homepage.bean.AttendanceResultBean
import com.rw.homepage.presenter.AttendancePresenter
import kotlinx.android.synthetic.main.hp_fragment_attendance.*
import kotlin.collections.ArrayList

/**
 * Created by Amuse
 * Date:2022/3/10.
 * Desc:
 */
class AttendanceFragment  : BaseFragment<AttendancePresenter>(){

    private var mMonth=0
    private var mYear=0
    companion object {
        private var instance: AttendanceFragment? = null

                fun getInstance(year: Int,month:Int):Fragment?{
                    instance=AttendanceFragment()
                    val build=Bundle()
                    build.putInt("month",month)
                    build.putInt("year",year)
                    instance?.arguments=build
                    return instance
                }

    }

    private val mAdapter : AttendanceAdapter by lazy {
        AttendanceAdapter()
    }

    override fun lazyData() {
        super.lazyData()
        arguments?.apply {
            mMonth=getInt("month",0)
            mYear=getInt("year",0)
            mPresenter?.getAttendance(AttendancePresenter.AttendanceReq("$mYear-$mMonth"))
        }

    }

    override fun getViewLayout(): Int {
        return R.layout.hp_fragment_attendance
    }

    override fun initView() {
        rv_attendance?.apply {
            layoutManager=GridLayoutManager(context,7)
            adapter=mAdapter
        }
    }

    override fun loadData() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_ATTENDANCE -> {
                    if (it is AttendanceResultBean) {
                        processDateResult(it.data)

                    }

                }
                else -> showToast("系统异常")
            }
        })
    }

    override fun getPresenter(): AttendancePresenter {
        return AttendancePresenter()
    }


   private  fun processDateResult(listAttendance:List<AttendanceBean>?){
       val weeks=mPresenter?.getWeekDay(mYear, mMonth)?:1//获取当月第一天是星期几
       val days=mPresenter?.getDays(mYear, mMonth)?:0+weeks//获取总天数
       var countDay=0
       val listData=ArrayList<AttendanceBean>()
       for (index in 1 until  days){
           if (index>=weeks){
               countDay++
               var attendanceBean:AttendanceBean?=null
               if (!listAttendance.isNullOrEmpty()){
                   for (i in listAttendance.indices){
                       val today= mPresenter?.getYearMonthDay(listAttendance[i].clockDate)?:"0"
                       if (countDay==today.toInt()){
                           attendanceBean=listAttendance[i]
                           attendanceBean.days=countDay.toString()
                           break
                       }
                   }
               }

               if (attendanceBean==null){
                   listData.add(AttendanceBean(0,"","","",0,"$countDay"))
               }else{
                   listData.add(attendanceBean)
               }

           }else{
               listData.add(AttendanceBean(0,"","","",0,""))
           }
       }
       mAdapter.setNewInstance(listData)
   }


}