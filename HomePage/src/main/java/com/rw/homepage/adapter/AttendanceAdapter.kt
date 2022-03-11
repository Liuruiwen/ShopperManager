package com.rw.homepage.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.homepage.R
import com.rw.homepage.bean.AttendanceBean

/**
 * Created by Amuse
 * Date:2022/3/10.
 * Desc:
 */
class AttendanceAdapter :
    BaseQuickAdapter<AttendanceBean, BaseViewHolder>(R.layout.hp_attendance_itme) {
    override fun convert(holder: BaseViewHolder, item: AttendanceBean) {
        holder.setGone(R.id.tv_date, !item.days.isNullOrEmpty())
        holder.setGone(R.id.tv_state, !item.days.isNullOrEmpty())
        holder.setText(R.id.tv_date, item.days)
        val isClock = !item.clockTime.isNullOrEmpty() && !item.afterWorkTime.isNullOrEmpty()
        holder.setText(R.id.tv_state, if (isClock) "已打卡" else "异常")
    }
}