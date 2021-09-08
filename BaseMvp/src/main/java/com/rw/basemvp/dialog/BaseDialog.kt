package com.rw.basemvp.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rw.basemvp.until.OnViewHolder
import com.rw.basemvp.until.ViewHolder
import com.rw.basemvp.R
import java.lang.ref.WeakReference

/**
 * Created by Amuse
 * Date:2020/3/29.
 * Desc:
 */
abstract class BaseDialog(context: Context, viewHelper: OnViewHolder?) :
    Dialog(context, R.style.AlertTipsDialogTheme) {
    var viewReference:WeakReference<Context>?=null

    init {
        viewReference = WeakReference(context)
        viewHelper?.let {
            getHelperView(null, getViewLayout(), it)?.apply {
                setContentView(this)
            }
        }

    }

    abstract fun getViewLayout(): Int

    /**
     * 实例化对应layoutId的view同时生成ViewHelper
     *
     * @param group    可为null
     * @param layoutId
     * @param listener
     * @return
     */
    protected  fun getHelperView(
        group: ViewGroup?,
        layoutId: Int,
        listener: OnViewHolder
    ): View? {
        val viewHolder = ViewHolder(LayoutInflater.from(getDialogContext()).inflate(layoutId, group, false))
        listener.helper(viewHolder)
        return viewHolder.getItemView()
    }


    /**
     * 设置参数的参考实现
     *
     * @param width
     * @param height
     * @param gravity
     */
    protected  fun setDialogParams(width: Int, height: Int, gravity: Int) {
        val window = this.window
        val params = window!!.attributes
        params.width = width
        params.height = height
        window.setGravity(gravity)
        window.attributes = params
    }



     fun getDialogContext(): Context? {
        return if (viewReference == null) null else viewReference?.get()
    }

    override fun onStop() {
        super.onStop()
            viewReference?.clear()
            viewReference = null

    }

}