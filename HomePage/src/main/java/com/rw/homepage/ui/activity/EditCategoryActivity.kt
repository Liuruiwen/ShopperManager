package com.rw.homepage.ui.activity

import android.os.Bundle
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.R
import com.rw.homepage.presenter.GoodsManagerPresenter

class EditCategoryActivity : BaseActivity<GoodsManagerPresenter>() {


    override fun setLayout(): Int {
       return R.layout.hp_activity_edit_category
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("品类管理")
    }

    override fun getPresenter(): GoodsManagerPresenter {
        return GoodsManagerPresenter()
    }
}