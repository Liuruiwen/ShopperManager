package com.rw.personalcenter.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.personalcenter.R
import com.rw.personalcenter.bean.EmployeesBean

/**
 * Created by Amuse
 * Date:2021/9/1.
 * Desc:
 */
class PersonalManagerAdapter :BaseQuickAdapter<EmployeesBean,BaseViewHolder>(R.layout.pc_item_employees) {
    override fun convert(holder: BaseViewHolder, item: EmployeesBean) {
        holder.setText(R.id.tv_account,"账号：${item.account}")
        holder.setText(R.id.tv_title,"职称：${item.nickName?:"无"}")
        holder.setText(R.id.tv_name,"姓名：${item.userName?:"无"}")
        holder.setText(R.id.tv_sex,"性别：${if(item.sex==0) "女" else "男"}")
        holder.setText(R.id.tv_age,"年龄：${item.age}岁")
        holder.setText(R.id.tv_desc,"地址：${item.address?:"无"}")
        addChildClickViewIds(R.id.tv_delete)

    }

    fun deleteItem(position:Int){
        if (data.size==0){
            data.removeAt(position)
            notifyDataSetChanged()
        }else{
            data.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeRemoved(position,data.size-position)
        }
    }
}