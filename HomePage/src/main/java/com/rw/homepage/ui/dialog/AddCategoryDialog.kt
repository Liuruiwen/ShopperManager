package com.rw.homepage.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import com.rw.basemvp.dialog.BaseWrapperDialog
import com.rw.homepage.R

/**
 * Created by Amuse
 * Date:2021/9/8.
 * Desc:用户编辑弹窗
 */
open class AddCategoryDialog (context: Context) : BaseWrapperDialog(context
){
    init {
        setDialogParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER)
    }
    override fun getViewLayout(): Int {
        return R.layout.hp_dialog_add_category
    }



}