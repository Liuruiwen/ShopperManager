package com.rw.homepage.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.homepage.R
import com.rw.homepage.bean.AttributeBean
import com.rw.homepage.bean.GoodsListBean
import com.rw.homepage.bean.NormsHeaderBean
import com.rw.homepage.until.setVisible

/**
 * Created by Amuse
 * Date:2021/9/15.
 * Desc:
 */
class GoodsListAdapter :BaseQuickAdapter<GoodsListBean,BaseViewHolder>(R.layout.hp_item_goods){
    override fun convert(holder: BaseViewHolder, item: GoodsListBean) {

        holder.setText(R.id.tv_name,item.goodsName)

        holder.setText(R.id.tv_desc,item.goodsDesc)
        holder.setText(R.id.tv_price,"价格:${item.goodsPrice}")
        holder.setText(R.id.tv_shelves,if (item.shelvesType==1) "下架" else "上架")
        val layoutAdd=holder.getView<LinearLayout>(R.id.layout_add)
        layoutAdd.setVisible(item.isShow)
        initAttr(layoutAdd,item.listNorms)

    }

    private fun initAttr(layoutAdd:LinearLayout,list:List<NormsHeaderBean>?){
        if (layoutAdd.childCount>0){
            layoutAdd.removeAllViews()
        }
        list?.forEach {
            layoutAdd.addView(getHeaderView(it.normsName?:""))
            it.listAttribute?.forEach{item->
                layoutAdd.addView(getAttrView(item.normsAttributeName))
            }
        }

    }

    private fun getHeaderView(name:String):View{
        val textView=LayoutInflater.from(context).inflate(R.layout.hp_item_header,null)
         if (textView is  TextView){
             textView.text=name
         }
        return textView
    }

    private fun getAttrView(attr:String):View{
        val textView=LayoutInflater.from(context).inflate(R.layout.hp_item_attr,null)
        if (textView is  TextView){
            textView.text=attr
        }
        return textView
    }

    fun updateShow(position:Int){
        data[position].isShow= !data[position].isShow
        notifyItemChanged(position)
    }
}