package com.rw.homepage.ui.activity

import android.os.Bundle
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.R
import com.rw.homepage.presenter.GoodsManagerPresenter

class GoodsSpecificationsActivity : BaseActivity<GoodsManagerPresenter>() {

    override fun setLayout(): Int {
        return R.layout.hp_activity_goods_specifications
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
       titleView.setTitle("商品规格")
    }

    override fun getPresenter(): GoodsManagerPresenter {
        return  GoodsManagerPresenter()
    }
}