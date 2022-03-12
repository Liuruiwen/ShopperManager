package com.rw.homepage.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.homepage.R
import com.rw.homepage.bean.AttendanceBean
import com.rw.homepage.until.setVisible

/**
 * Created by Amuse
 * Date:2022/3/10.
 * Desc:
 */
class AttendanceAdapter : BaseQuickAdapter<AttendanceBean, BaseViewHolder>(R.layout.hp_attendance_itme) {
    override fun convert(holder: BaseViewHolder, item: AttendanceBean) {
        holder.getView<TextView>(R.id.tv_date).setVisible(!item.days.isNullOrEmpty())
        holder.getView<TextView>(R.id.tv_state).setVisible(!item.days.isNullOrEmpty())
        holder.setText(R.id.tv_date, item.days)
        val isClock = !item.clockTime.isNullOrEmpty() && !item.afterWorkTime.isNullOrEmpty()
        holder.setText(R.id.tv_state, if (isClock) "已打卡" else "未打卡")
        holder.setTextColor(R.id.tv_state,ContextCompat.getColor(context,if (isClock)R.color.colorPrimary else R.color.textLight))
    }
}