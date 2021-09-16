package com.rw.homepage.ui.fragment

import android.annotation.SuppressLint
import com.alibaba.android.arouter.facade.annotation.Route
import com.rw.basemvp.BaseWrapperFragment
import com.rw.homepage.R
import com.rw.homepage.presenter.HomePagePresenter
import com.rw.homepage.ui.activity.GoodsManagerActivity
import kotlinx.android.synthetic.main.hp_fragment_home.*
import org.jetbrains.anko.startActivity

/**
 * Created by Amuse
 * Date:2021/4/15.
 * Desc:
 */
@Route(path = "/home/homePageFragment")
 class HomePageFragment : BaseWrapperFragment<HomePagePresenter>() {
    override fun getViewLayout(): Int {
        return R.layout.hp_fragment_home
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {


        click()

    }

    override fun loadData() {

    }

    override fun getPresenter(): HomePagePresenter {
        return HomePagePresenter()
    }

    override fun lazyData() {

    }


    private fun click(){
        tv_goods_manager.setOnClickListener {
            mContext?.startActivity<GoodsManagerActivity>()
        }
    }
}