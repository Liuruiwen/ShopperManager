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
class OrderVpAdapter<T:Fragment>(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle) {

    private var listFragment=ArrayList<T>()

    fun addFragment(fragment:T){
       listFragment.add(fragment)
        notifyDataSetChanged()
    }

    fun setNewData(list: ArrayList<T>?){
        list?.let {
            listFragment=it
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
       return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
       return listFragment[position]
    }
}