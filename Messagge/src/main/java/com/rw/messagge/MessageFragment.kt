package com.rw.messagge

import android.annotation.SuppressLint
import com.alibaba.android.arouter.facade.annotation.Route
import com.rw.basemvp.BaseWrapperFragment
import com.rw.messagge.presenter.MessagePresenter

/**
 * Created by Amuse
 * Date:2021/4/15.
 * Desc:
 */
@Route(path = "/message/MessageCenter")
 class MessageFragment : BaseWrapperFragment<MessagePresenter>() {
    override fun getViewLayout(): Int {
        return R.layout.mc_message
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {



    }

    override fun loadData() {

    }

    override fun getPresenter(): MessagePresenter {
        return MessagePresenter()
    }

    override fun lazyData() {

    }
}