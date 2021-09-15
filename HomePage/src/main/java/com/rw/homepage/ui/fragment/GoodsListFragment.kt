package com.rw.homepage.ui.fragment

import com.rw.basemvp.BaseFragment
import com.rw.homepage.R
import com.rw.homepage.presenter.GoodsListPresenter

/**
 * Created by Amuse
 * Date:2021/9/15.
 * Desc:商品列表
 */
class GoodsListFragment :BaseFragment<GoodsListPresenter>(){
    override fun getViewLayout(): Int {
     return R.layout.hp_fragment_goods_list
    }

    override fun initView() {

    }

    override fun loadData() {

    }

    override fun getPresenter(): GoodsListPresenter {
      return  GoodsListPresenter()
    }
}