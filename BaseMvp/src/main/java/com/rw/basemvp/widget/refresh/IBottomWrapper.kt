package com.rw.basemvp.widget.refresh

import android.view.View

/**
 * Created by Amuse
 * Date:2020/4/1.
 * Desc:
 */
interface IBottomWrapper {
    /**
     * 获取无更多布局
     *
     * @return
     */
    fun getBottomView(): View
    /**
     * 显示无更多布局
     */
    fun showBottom()

}