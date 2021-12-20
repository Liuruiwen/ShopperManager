package com.rw.homepage.presenter

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.rw.basemvp.presenter.BaseView
import com.rw.basemvp.presenter.MvpPresenter
import com.rw.basemvp.until.ViewHolder
import com.rw.homepage.R
import com.rw.homepage.ui.dialog.MessageDialog

/**
 * Created by Amuse
 * Date:2021/4/15.
 * Desc:
 */
open class HomePagePresenter:MvpPresenter<BaseView> (){
    override fun getBaseUrl(): String {
       return "http://192.168.1.4:8080"
    }

    override fun getToken(): String {
       return ""
    }

    override fun onCreate(owner: LifecycleOwner) {

    }

    override fun onStart(owner: LifecycleOwner) {

    }

    override fun onStop(owner: LifecycleOwner) {

    }

    override fun onResume(owner: LifecycleOwner) {

    }

    override fun onPause(owner: LifecycleOwner) {

    }


    fun showMessageDialog(context: Context, click:View.OnClickListener?,
                           cancelId: Int, confirm: Int
    ): MessageDialog {

        return object :MessageDialog(context){
            override fun helper(helper: ViewHolder?) {
                super.helper(helper)
                helper?.setOnClickListener(click,cancelId,confirm)
            }
        }
    }
}