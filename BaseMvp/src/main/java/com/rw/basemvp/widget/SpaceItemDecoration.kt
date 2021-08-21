package com.rw.basemvp.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Amuse
 * Date:2020/3/14.
 * Desc:RecyclerView 间距工具类
 */
class SpaceItemDecoration constructor(private  val space:Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == 0) {
            //item是第一个的时候不设置间距
            outRect.left = 0;
        }else {
            outRect.left = space;
        }
    }
}

