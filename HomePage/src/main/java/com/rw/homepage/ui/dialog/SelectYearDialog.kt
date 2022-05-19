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
 * Date:2022/5/19.
 * Desc:
 */
open class SelectYearDialog  (context: Context) : BaseWrapperDialog(context
){

    init {
        setDialogParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER)
    }
    override fun getViewLayout(): Int {
        return R.layout.hp_dialog_select_year
    }




}