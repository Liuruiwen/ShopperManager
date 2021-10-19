package com.rw.homepage.ui.activity

import android.os.Bundle
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.R
import com.rw.homepage.presenter.GoodsEditPresenter
const val GOODS_EDIT_TYPE_ADD=1//增加商品
const val GOODS_EDIT_TYPE_EDIT=2//编辑商品
class GoodsEditActivity : BaseActivity<GoodsEditPresenter>() {

private val categroyId :Int by lazy {
    intent.getIntExtra("id",0)
}
    override fun setLayout(): Int {
        return R.layout.hp_activity_goods_edit
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        val type=intent.getIntExtra("type",GOODS_EDIT_TYPE_ADD)
        titleView.setTitle(if (type==GOODS_EDIT_TYPE_ADD)"添加商品" else "商品编辑")
    }

    override fun getPresenter(): GoodsEditPresenter {
       return  GoodsEditPresenter()
    }
}