@file:Suppress("DEPRECATION")

package com.rw.basemvp.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.rw.basemvp.R


/*   
* @Author:      Amuser
* @CreateDate:   2019-12-20 15:22
*@Description: 
*/
class LoadingDialog(context: Context?) : AlertDialog(context, R.style.Alert_Dialog_Style) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.base_dialog_loading_progressly)//loading的xml文件
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.WRAP_CONTENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params
        window?.setBackgroundDrawableResource(android.R.color.transparent)//去掉白色背景

    }

}