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
 * Date:2022/3/15.
 * Desc:
 */
open class CommitCardDialog (context: Context): BaseWrapperDialog(context) {
    var tvClock: TextView?=null
    var tvAfterClock: TextView?=null
    init {
        setDialogParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER)
    }
    override fun getViewLayout(): Int {
        return R.layout.hp_dialog_card_commit
    }
    override fun helper(helper: ViewHolder?) {
        super.helper(helper)
        tvClock=helper?.getView(R.id.tv_clock_time)
        tvAfterClock=helper?.getView(R.id.tv_after_clock)
    }

    fun showDialog(clockTime:String,afterClockTime:String){
        tvClock?.text="上班打卡时间：${clockTime}"
        tvAfterClock?.text="下班打卡时间：${afterClockTime}"
        show()
    }
}