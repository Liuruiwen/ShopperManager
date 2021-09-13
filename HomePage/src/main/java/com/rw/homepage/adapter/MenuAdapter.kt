package com.rw.homepage.adapter

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.homepage.R
import com.rw.homepage.bean.CategoryBean
import com.rw.homepage.bean.CategoryResultBean

/**
 * Created by Amuse
 * Date:2021/9/13.
 * Desc:
 */
class MenuAdapter:BaseQuickAdapter<CategoryResultBean,BaseViewHolder>(R.layout.hp_item_menu) {
    private var selectPosition=0
    override fun convert(holder: BaseViewHolder, item: CategoryResultBean) {
        val tvMenu=holder.getView<TextView>(R.id.tv_menu)
        tvMenu.text=item.categoryName
        tvMenu.setTextColor(ContextCompat.getColor(context,if (selectPosition==holder.adapterPosition) R.color.textMain else R.color.textLight))
        tvMenu.typeface = if (selectPosition==holder.adapterPosition)
            Typeface.defaultFromStyle(Typeface.BOLD)
        else Typeface.defaultFromStyle(Typeface.NORMAL)
        addChildClickViewIds(R.id.tv_menu)

    }

    fun setSelectItem(position:Int){
        selectPosition=position
        notifyDataSetChanged()
    }
}