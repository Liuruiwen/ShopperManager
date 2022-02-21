package com.rw.homepage.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by Amuse
 * Date:2022/2/21.
 * Desc:
 */
class OrderVpAdapter(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle) {

    private var listFragment=ArrayList<Fragment>()

    fun addFragment(fragment:Fragment){
       listFragment.add(fragment)
        notifyDataSetChanged()
    }

    fun setNewData(list: ArrayList<Fragment>){
        listFragment=list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
       return listFragment[position]
    }
}