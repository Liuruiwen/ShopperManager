package com.rw.personalcenter.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.personalcenter.R
import com.rw.personalcenter.bean.RequestCardItem


/**
 * Created by Amuse
 * Date:2022/3/16.
 * Desc:
 */
class RequestCardAdapter:BaseQuickAdapter<RequestCardItem,BaseViewHolder>(R.layout.hp_item_request_card) {
    override fun convert(holder: BaseViewHolder, item: RequestCardItem) {
        holder.setText(R.id.tv_card_time,"提交补卡时间：${item.cardTime}")
        holder.setText(R.id.tv_absenteeism_time,"缺勤时间：${item.absenteeismTime}")
        holder.setText(R.id.tv_account,"账号：${item.account}")
        val tvApproval=holder.getView<TextView>(R.id.tv_approval)
        val drawable = tvApproval.background
        if (drawable is GradientDrawable){
            if (item.cardState==1){
                drawable.setColor(Color.parseColor("#999999"))
                tvApproval.setTextColor(ContextCompat.getColor(context,R.color.colorLine))
            }else{
                drawable.setColor(Color.parseColor("#33a3dc"))
                tvApproval.setTextColor(ContextCompat.getColor(context,R.color.colorWrite))
            }
        }

    }
}