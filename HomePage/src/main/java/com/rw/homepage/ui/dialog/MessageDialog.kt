package com.rw.homepage.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.rw.basemvp.dialog.BaseWrapperDialog
import com.rw.basemvp.until.ViewHolder
import com.rw.homepage.R

/**
 * Created by Amuse
 * Date:2021/12/11.
 * Desc:message 弹窗
 */
open class MessageDialog  (context: Context) : BaseWrapperDialog(context
){
    var tvMessage:TextView?=null
    init {
        setDialogParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER)
    }
    override fun getViewLayout(): Int {
        return R.layout.hp_item_message_dialog
    }

    override fun helper(helper: ViewHolder?) {
        super.helper(helper)
        tvMessage=helper?.getView(R.id.tv_message)
    }

    fun showDialog(message:String){
        tvMessage?.text=message
        show()
    }


}