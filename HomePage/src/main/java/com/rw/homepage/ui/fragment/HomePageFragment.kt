package com.rw.homepage.ui.fragment

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.rw.basemvp.BaseWrapperFragment
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.bean.GoodsBean
import com.rw.homepage.bean.ShopperResultBean
import com.rw.homepage.presenter.HomePagePresenter
import com.rw.homepage.ui.activity.GoodsManagerActivity
import com.rw.homepage.until.setVisible
import kotlinx.android.synthetic.main.hp_empty_state.*
import kotlinx.android.synthetic.main.hp_fragment_goods_list.*
import kotlinx.android.synthetic.main.hp_fragment_home.*
import org.jetbrains.anko.startActivity
import java.nio.channels.FileChannel

/**
 * Created by Amuse
 * Date:2021/4/15.
 * Desc:
 */
@Route(path = "/home/homePageFragment")
 class HomePageFragment : BaseWrapperFragment<HomePagePresenter>() {

    private var resultBean:ShopperResultBean?=null
    override fun getViewLayout(): Int {
        return R.layout.hp_fragment_home
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {


        click()
        mPresenter?.getShopperDetail()
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_SHOPPER_DETAIL -> {
                    if (it is ShopperResultBean){
                        resultBean=it
                    }


                }
                else -> showToast("系统异常")
            }
        })

    }

    override fun loadData() {

    }

    override fun getPresenter(): HomePagePresenter {
        return HomePagePresenter()
    }

    override fun lazyData() {

    }


    private fun click(){
        /**
         * 商品管理
         */
        tv_goods_manager.setOnClickListener {
            mContext?.startActivity<GoodsManagerActivity>()
        }
        /**
         * 员工打卡
         */
        tv_clock_work.setOnClickListener {
            ARouter.getInstance().build("/map/ClockActivity")
                .withString("latitude",resultBean?.data?.address?.latitude?:"")
                .withString("longitude",resultBean?.data?.address?.longitude?:"")
                .navigation()
        }
    }
}