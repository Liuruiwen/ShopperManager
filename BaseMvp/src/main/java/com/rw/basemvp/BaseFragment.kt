package com.rw.basemvp

import com.rw.basemvp.presenter.BaseView
import com.rw.basemvp.presenter.MvpPresenter

/**
 * Created by Amuse
 * Date:2020/2/12.
 * Desc:
 */
abstract  class BaseFragment<P:MvpPresenter<BaseView>> :BaseWrapperFragment<P>(){
    override fun onShowLoading() {
        super.onShowLoading()
    }

    override fun lazyData() {
//       showToast("第一次加载哦")
    }





}