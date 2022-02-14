package com.rw.homepage.ui.fragment

import com.rw.basemvp.BaseFragment
import com.rw.homepage.R
import com.rw.homepage.presenter.OrderListPresenter

/**
 * Created by Amuse
 * Date:2022/2/9.
 * Desc:
 */
class OrderListFragment : BaseFragment<OrderListPresenter>(){
    override fun getViewLayout(): Int {
       return R.layout.hp_fragment_order_list
    }

    override fun initView() {

    }

    override fun loadData() {

    }

    override fun getPresenter(): OrderListPresenter {
        return OrderListPresenter()
    }
}