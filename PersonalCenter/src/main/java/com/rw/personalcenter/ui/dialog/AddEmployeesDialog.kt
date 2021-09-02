package com.rw.personalcenter.ui.dialog

import android.content.Context
import com.rw.basemvp.dialog.BaseDialog
import com.rw.basemvp.until.OnViewHolder
import com.rw.personalcenter.R

/**
 * Created by Amuse
 * Date:2021/9/2.
 * Desc:添加员工dialog
 */
class AddEmployeesDialog(context: Context, viewHelper: OnViewHolder) :BaseDialog(context,
    viewHelper
){
    override fun getViewLayout(): Int {
        return R.layout.pc_item_employees
    }
}