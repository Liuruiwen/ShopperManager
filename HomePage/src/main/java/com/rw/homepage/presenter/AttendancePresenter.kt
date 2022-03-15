package com.rw.homepage.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.homepage.HttpApi
import com.rw.homepage.bean.AttendanceResultBean
import com.rw.homepage.bean.OrderListResultBean
import com.rw.service.ServiceViewModule
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Amuse
 * Date:2022/3/10.
 * Desc:
 */
class AttendancePresenter :HomePagePresenter(){

    /**
     * 获取规格属性列表
     */
    fun getAttendance(req: AttendanceReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_ATTENDANCE, AttendanceResultBean::class.java, true,
                mapOf("token" to bean.token),req
            )
        }

    }


    /**
     * 提交补卡信息
     */
    fun commitCard(req: String) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_COMMIT_CARD, BaseBean::class.java, true,
                mapOf("token" to bean.token),CommitCardReq(req)
            )
        }

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
     fun getDays(year: Int, month: Int): Int {
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
     fun getWeekDay(year: Int, month: Int): Int {
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

    data class AttendanceReq(
        var time:String
    )

    data class CommitCardReq(
        var absenteeismTime:String
    )
}