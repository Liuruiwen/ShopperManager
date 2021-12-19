package com.rw.homepage.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.Gson
import com.rw.homepage.R
import com.rw.homepage.bean.MultiItemBean
import com.rw.homepage.bean.NormsAttributeBean
import com.rw.homepage.bean.NormsHeaderBean
import com.rw.homepage.bean.NormsId

/**
 * Created by Amuse
 * Date:2021/12/19.
 * Desc:商品编辑Adapter
 */
class GoodsEditNormsAdapter : BaseMultiItemQuickAdapter<MultiItemBean, BaseViewHolder>() {
    init {
        addItemType(TYPE_NORMS_ITEM_HEADER, R.layout.hp_item_header)
        addItemType(TYPE_NORMS_ITEM_ATTRIBUTE, R.layout.hp_item_attr)
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
        holder.setText(R.id.tv_attr,item.normsName)
    }

    /**
     * 处理属性
     */
    private fun processAttribute(holder: BaseViewHolder, item: NormsAttributeBean){
        holder.setText(R.id.tv_attr,item.normsAttributeName)

    }



    fun getNormsId() :String?{
        val list=ArrayList<NormsId>()
        var id=0
        val listAttrId=ArrayList<Int>()
        data.forEach {
            if (it is NormsHeaderBean){
                if (id==0){
                    id=it.id
                }else{
                    list.add(NormsId(id,listAttrId))
                }
                listAttrId.clear()
            }else if (it is NormsAttributeBean){
                listAttrId.add(it.id)
            }
        }

        return Gson().toJson(list)
    }
}