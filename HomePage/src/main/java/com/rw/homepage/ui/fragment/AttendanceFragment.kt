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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
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
                        if (!it.data.isNullOrEmpty()){
                            val weeks=getWeekDay(mYear, mMonth)//获取当月第一天是星期几
                            val days= getDays(mYear, mMonth)+weeks-1//获取总天数
                            var countDay=0
                            val listData=ArrayList<AttendanceBean>()
                            for (index in 1 until  days){
                                if (weeks<=index){
                                    countDay++
                                    var attendanceBean:AttendanceBean?=null
                                    for (i in 0 until it.data!!.size){
                                      val today=  getYearMonthDay(it.data!![i].clockDate)
                                       if (index==today?.toInt()){
                                           attendanceBean=it.data!![i]
                                           attendanceBean.days=index.toString()
                                           break
                                       }
                                    }
                                    if (attendanceBean==null){
                                        listData.add(AttendanceBean(0,"","","",0,"$index"))
                                    }else{
                                        listData.add(attendanceBean)
                                    }
                                    if (countDay==days){

                                        break
                                    }
                                }else{
                                    listData.add(AttendanceBean(0,"","","",0,""))
                                }
                            }
                            mAdapter.setNewInstance(listData)
                        }

                    }

                }
                else -> showToast("系统异常")
            }
        })
    }

    override fun getPresenter(): AttendancePresenter {
        return AttendancePresenter()
    }


    private fun getAttendanceBean():AttendanceBean{

        return getAttendanceBean()
    }

    /**
     * 平年月天数数组
     */
    var commonYearMonthDay =
        intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    /**
     * 闰年月天数数组
     */
    var leapYearMonthDay =
        intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    /**
     * https://cloud.tencent.com/developer/article/1809973 日历
     * 通过年月，获取这个月一共有多少天
     */
    private fun getDays(year: Int, month: Int): Int {
        var days = 0
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            if (month > 0 && month <= 12) {
                days = leapYearMonthDay.get(month - 1)
            }
        } else {
            if (month > 0 && month <= 12) {
                days = commonYearMonthDay.get(month - 1)
            }
        }
        return days
    }

    /**
     * 通过年，月获取当前月的第一天为星期几 ，返回0是星期天，1是星期一，依次类推
     */
    private fun getWeekDay(year: Int, month: Int): Int {
        val dayOfWeek: Int
        var goneYearDays = 0
        var thisYearDays = 0
        var isLeapYear = false //闰年
        for (i in 1900 until year) { // 从1900年开始算起，1900年1月1日为星期一
            goneYearDays = if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                goneYearDays + 366
            } else {
                goneYearDays + 365
            }
        }
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            isLeapYear = true
            for (i in 0 until month - 1) {
                thisYearDays = thisYearDays + leapYearMonthDay[i]
            }
        } else {
            isLeapYear = false
            for (i in 0 until month - 1) {
                thisYearDays = thisYearDays + commonYearMonthDay[i]
            }
        }
        dayOfWeek = (goneYearDays + thisYearDays + 1) % 7
//        Log.d(this.javaClass.name, "从1990到现在有" + (goneYearDays + thisYearDays + 1) + "天")
//        Log.d(
//            this.javaClass.name,
//            year.toString() + "年" + month + "月" + 1 + "日是星期" + dayOfWeek
//        )
        return dayOfWeek
    }

    /**
     * 获取年月日
     *
     * @return
     */
    fun getYearMonthDay(time: String?): String? {
        try {
            val df = SimpleDateFormat("yyyy-MM-dd").parse(time)
            val ca = Calendar.getInstance()
            ca.setTime(df)
            val day: Int = ca.get(Calendar.DATE) //一年中的第几天
            return day.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return "0"
    }

}