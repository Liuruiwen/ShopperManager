package com.rw.homepage.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.homepage.R
import com.rw.homepage.bean.MultiItemBean
import com.rw.homepage.bean.NormsAttributeBean
import com.rw.homepage.bean.NormsHeaderBean

/**
 * Created by Amuse
 * Date:2021/12/4.
 * Desc:
 */
const val TYPE_NORMS_ITEM_HEADER=1
const val TYPE_NORMS_ITEM_ATTRIBUTE=2
class NormsListAdapter: BaseMultiItemQuickAdapter<MultiItemBean,BaseViewHolder>() {
    init {
        addItemType(TYPE_NORMS_ITEM_HEADER, R.layout.hp_item_norms_header)
        addItemType(TYPE_NORMS_ITEM_ATTRIBUTE, R.layout.hp_item_norms_attribute)
    }
    override fun convert(holder: BaseViewHolder, item: MultiItemBean) {
                 when(holder.itemViewType){
                     TYPE_NORMS_ITEM_HEADER->{//头部
                         if (item is NormsHeaderBean){
                             processHeader(holder, item)
                         }

                     }
                     TYPE_NORMS_ITEM_ATTRIBUTE->{//主体
                         if (item is NormsAttributeBean){
                             processAttribute(holder, item)
                         }

                     }
                 }

    }

    /**
     * 处理头部
     */
    private fun processHeader(holder: BaseViewHolder, item: NormsHeaderBean){
        holder.setText(R.id.tv_name,item.normsName)
    }

    /**
     * 处理属性
     */
    private fun processAttribute(holder: BaseViewHolder, item: NormsAttributeBean){
          holder.setBackgroundResource(R.id.tv_attribute_name,if (item.selectType==1)   R.drawable.hp_button_bg_blue else R.drawable.hp_circle_bg_white)
          holder.setTextColor(R.id.tv_attribute_name,ContextCompat.getColor(context,if (item.selectType==1)   R.color.colorWrite else R.color.textGray) )
          holder.setText(R.id.tv_attribute_name,item.normsAttributeName)
    }

    fun attributeClick(position:Int){
        val item=data[position]
        if (item is NormsAttributeBean){
            item.selectType=if (item.selectType==1) 0 else 1
            notifyItemChanged(position)
        }
    }

    fun isSelectAttribute():Boolean{
        for (index in 0 until  data.size){
            val item=data[index]
            if (item is NormsAttributeBean && item.selectType==1){
                return true
            }
        }

        return false
    }
}