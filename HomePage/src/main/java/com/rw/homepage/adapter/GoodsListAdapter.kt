package com.rw.homepage.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ruiwenliu.glide.library.GlideManager
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
        GlideManager
            .getInstance(context)?.loadRoundImage(item.goodsImage,holder.getView<ImageView>(R.id.iv_goods),15)
        holder.setText(R.id.tv_desc,item.goodsDesc)
        holder.setText(R.id.tv_price,"价格:${item.goodsPrice}")
        holder.setText(R.id.tv_shelves,if (item.shelvesType==1) "下架" else "上架")
        val layoutAdd=holder.getView<LinearLayout>(R.id.layout_add)
//        layoutAdd.setVisible(item.isShow)
//        holder.setText(R.id.tv_show,if (!item.isShow) "展开" else "收起")
//        holder.setTextColor(R.id.tv_show,ContextCompat.getColor(context,if (!item.isShow) R.color.colorPrimary else R.color.textLight))
        initAttr(layoutAdd,item.listNorms)

    }

    private fun initAttr(layoutAdd:LinearLayout,list:List<NormsHeaderBean>?){
        if (layoutAdd.childCount>0){
            layoutAdd.removeAllViews()
        }
        list?.forEach {
            layoutAdd.addView(getHeaderView(it.normsName?:""))
            val tvName=LayoutInflater.from(context).inflate(R.layout.hp_item_attr,null)
            val stringBuilder=StringBuilder()
            it.listAttribute?.forEach{item->
                stringBuilder.append(item.normsAttributeName)
                stringBuilder.append("  ")

            }
            if (tvName is TextView){
                tvName.text=stringBuilder.toString()
                layoutAdd.addView(tvName)
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



    fun updateShow(position:Int){
        data[position].isShow= !data[position].isShow
        notifyItemChanged(position)
    }

    fun updateItem(position: Int,item:GoodsListBean){
        data[position]=item
        notifyItemChanged(position)
    }
}