package com.rw.homepage.adapter

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rw.homepage.R
import com.rw.homepage.bean.GoodsListItemBean
import com.rw.homepage.bean.OrderItemBean

/**
 * Created by Amuse
 * Date:2022/2/9.
 * Desc:
 */
class OrderListAdapter :BaseQuickAdapter<OrderItemBean,BaseViewHolder>(R.layout.hp_item_order_list){

    override fun convert(holder: BaseViewHolder, item: OrderItemBean) {

       holder.setText(R.id.tv_order,"订单号：${item.id}")
        holder.setText(R.id.tv_time,item.orderTime)
        holder.setText(R.id.tv_price,item.goodsPrice)
        holder.setGone(R.id.tv_state,item.orderState==0)
        val goodsLayout=holder.getView<LinearLayout>(R.id.layout_goods_add)
        addGoods(goodsLayout,item.goodsList)
    }


    private fun addGoods(layout:LinearLayout?,list:List<GoodsListItemBean>?){
        if (list.isNullOrEmpty()){
            return
        }
        layout?.removeAllViews()
        list.forEach {
            val view=LayoutInflater.from(context).inflate(R.layout.hp_item_order_goods,null)
            val tvGoods=view.findViewById<TextView>(R.id.tv_goods)
            val stringBuild=StringBuilder()
            stringBuild.append(it.goodsName)
            stringBuild.append("：")
            it.normsAttributeList?.forEach {attribute->
                stringBuild.append(attribute.normsAttributeName)
                stringBuild.append("& #160;& #160;")
            }
            tvGoods.text=stringBuild.toString()
            layout?.addView(view)
        }

    }
}