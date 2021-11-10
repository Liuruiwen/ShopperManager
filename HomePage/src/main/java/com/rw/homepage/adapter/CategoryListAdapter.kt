package com.rw.homepage.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.BaseDraggableModule
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.homepage.R
import com.rw.homepage.bean.CategoryResultBean

/**
 * Created by Amuse
 * Date:2021/10/19.
 * Desc:
 */
class CategoryListAdapter :BaseQuickAdapter<CategoryResultBean,BaseViewHolder>(R.layout.hp_item_category_list),
    DraggableModule {
    override fun convert(holder: BaseViewHolder, item: CategoryResultBean) {
        holder.setText(R.id.tv_position,"${holder.adapterPosition}")
        holder.setText(R.id.tv_category_name,item.categoryName)
        holder.setText(R.id.tv_category_desc,item.categoryDesc)
        holder.setTextColor(R.id.tv_position,ContextCompat.getColor(context,R.color.colorWrite))

    }

    fun moveItem(position:Int){
        data.removeAt(position)
        if (data.size==0){
            notifyDataSetChanged()
        }else{
            notifyItemRemoved(position)
            notifyItemRangeRemoved(position,data.size-position)
        }
    }


}