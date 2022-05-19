package com.rw.personalcenter.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import com.rw.basemvp.dialog.BaseWrapperDialog
import com.rw.personalcenter.R

/**
 * Created by Amuse
 * Date:2022/5/19.
 * Desc:
 */
open class CameraDialog(context: Context) : BaseWrapperDialog(context
){
    init {
        setDialogParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM)
    }
    override fun getViewLayout(): Int {
        return R.layout.pc_dialog_camera
    }



}