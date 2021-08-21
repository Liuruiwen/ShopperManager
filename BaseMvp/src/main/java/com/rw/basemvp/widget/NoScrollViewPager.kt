package com.rw.basemvp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by Amuse
 * Date:2020/2/29.
 * Desc:
 */
class NoScrollViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    var  isScroll:Boolean=true

    public fun setScrollabele(bool:Boolean){
        this.isScroll=bool;
    }

    public  fun isScrollable():Boolean{
        return isScroll
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (isScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

}