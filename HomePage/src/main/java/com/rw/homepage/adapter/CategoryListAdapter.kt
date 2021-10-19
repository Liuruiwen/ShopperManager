package com.rw.homepage.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.homepage.R
import com.rw.homepage.bean.CategoryResultBean

/**
 * Created by Amuse
 * Date:2021/10/19.
 * Desc:
 */
class CategoryListAdapter :BaseQuickAdapter<CategoryResultBean,BaseViewHolder>(R.layout.hp_item_category_list) {
    override fun convert(holder: BaseViewHolder, item: CategoryResultBean) {
        holder.setText(R.id.tv_position,"${holder.adapterPosition}")
        holder.setText(R.id.tv_category_name,item.categoryName)
        holder.setText(R.id.tv_category_desc,item.categoryDesc)
    }
}