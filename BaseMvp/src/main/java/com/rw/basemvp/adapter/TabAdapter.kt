package com.rw.basemvp.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by Amuse
 * Date:2020/2/11.
 * Desc:
 */
@SuppressLint("WrongConstant")
class TabAdapter constructor (fm: FragmentManager, var list:List<Fragment>) : FragmentStatePagerAdapter(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var position: Int = 0


    override fun getItem(i: Int): Fragment {
       return list[i]
    }

    override fun getCount(): Int {
       return if (list == null) 0 else list.size;
    }

    override fun setPrimaryItem(container: ViewGroup, i: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        position = i;
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    public fun getCurrentFragment():Fragment {
        return list[position]
    }

    public  fun  refreshDada(listData:List<Fragment>){
        list=listData;
        notifyDataSetChanged();
    }
}