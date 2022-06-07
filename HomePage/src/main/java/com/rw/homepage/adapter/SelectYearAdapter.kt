package com.rw.homepage.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.homepage.R

/**
 * Created by Amuse
 * Date:2022/6/4.
 * Desc:选中年份适配器
 */
class SelectYearAdapter:BaseQuickAdapter<String,BaseViewHolder>(R.layout.hp_item_flipper_view) {
    private var selectItem:String?=""
    override fun convert(holder: BaseViewHolder, item: String) {
       holder.setTextColor(R.id.tv_flipper, ContextCompat.getColor(context,if (selectItem==item) R.color.colorPrimary else R.color.textMain))
       holder.setText(R.id.tv_flipper,item)
    }

    fun setSelectItem(item:String){
        selectItem=item
    }
}