package com.rw.personalcenter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.rw.personalcenter.R
import com.rw.personalcenter.bean.LevelBean


/**
 * Created by Amuse
 * Date:2021/9/4.
 * Desc:
 */
class SpinnerAdapter constructor(val context: Context,val list: List<LevelBean>?) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val  view = LayoutInflater.from(context).inflate(R.layout.pc_spinner_item, null)
        if (view != null) {
            val textView=view.findViewById<TextView>(R.id.tv_name)
            textView.text=list?.get(position)?.nickName
        }
        return view!!
    }

    override fun getItem(position: Int): Any? {
     return   list?.get(position)
    }

    override fun getItemId(position: Int): Long {
            return position.toLong()
    }

    override fun getCount(): Int {
       return if (!list.isNullOrEmpty()) list.size else 0
    }



}