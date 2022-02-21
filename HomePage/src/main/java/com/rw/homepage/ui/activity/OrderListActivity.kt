package com.rw.homepage.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.R
import com.rw.homepage.adapter.MenuAdapter
import com.rw.homepage.adapter.OrderVpAdapter
import com.rw.homepage.bean.CategoryResultBean
import com.rw.homepage.presenter.HomePagePresenter
import com.rw.homepage.ui.fragment.OrderListFragment
import kotlinx.android.synthetic.main.hp_activity_goods_manager.rv_menu
import kotlinx.android.synthetic.main.hp_activity_order_list.*

/**
 * Created by Amuse
 * Date:2022/2/16.
 * Desc:
 */
class OrderListActivity: BaseActivity<HomePagePresenter>() {
    private val menuAdapter:MenuAdapter by lazy {
        MenuAdapter()
    }

    private val vpAdapter:OrderVpAdapter by lazy {
        OrderVpAdapter(supportFragmentManager,lifecycle)
    }
    override fun getPresenter(): HomePagePresenter {
        return HomePagePresenter()
    }

    override fun setLayout(): Int {
        return R.layout.hp_activity_order_list
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("订单列表")
        initView()
    }

    private fun initView(){
        /**
         * 菜单处理
         */
        rv_menu.apply {
            layoutManager= LinearLayoutManager(this@OrderListActivity).apply {
                orientation= LinearLayoutManager.HORIZONTAL
            }
            adapter=menuAdapter
        }
        val listMenu=ArrayList<CategoryResultBean>()
        listMenu.add(menuItem("待处理",1))
        listMenu.add(menuItem("已完成",2))
        listMenu.add(menuItem("已退款",3))
        menuAdapter.setNewInstance(listMenu)
        /**
         * viewpager处理
         */
        rv_viewPage?.apply {
            adapter=vpAdapter
        }
        vpAdapter.setNewData(arrayListOf(OrderListFragment(),OrderListFragment(),OrderListFragment()))
    }
    private fun menuItem(name:String,id:Int):CategoryResultBean{
        return CategoryResultBean(id,name,0,"","")
    }
}