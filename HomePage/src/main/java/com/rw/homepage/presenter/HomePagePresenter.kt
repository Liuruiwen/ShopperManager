package com.rw.homepage.presenter

import android.content.Context
import android.view.View
import com.rw.basemvp.presenter.DefaultPresenter
import com.rw.basemvp.until.ViewHolder
import com.rw.homepage.HttpApi
import com.rw.homepage.bean.ShopperResultBean
import com.rw.homepage.ui.dialog.MessageDialog
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2021/4/15.
 * Desc:
 */
open class HomePagePresenter: DefaultPresenter(){
    override fun getBaseUrl(): String {
       return "http://192.168.1.4:8080"
    }

    /**
     * 获取规格属性列表
     */
    fun getShopperDetail() {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_SHOPPER_DETAIL, ShopperResultBean::class.java, true,
                linkedMapOf("token" to bean.token)
            )
        }

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