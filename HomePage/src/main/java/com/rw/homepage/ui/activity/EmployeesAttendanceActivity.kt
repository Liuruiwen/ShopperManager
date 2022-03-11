package com.rw.homepage.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.R
import com.rw.homepage.adapter.MenuAdapter
import com.rw.homepage.adapter.OrderVpAdapter
import com.rw.homepage.bean.CategoryResultBean
import com.rw.homepage.presenter.HomePagePresenter
import com.rw.homepage.ui.fragment.AttendanceFragment
import kotlinx.android.synthetic.main.hp_activity_employees_attendance.*
import kotlinx.android.synthetic.main.hp_activity_goods_manager.rv_menu

class EmployeesAttendanceActivity : BaseActivity<HomePagePresenter>() {

    private val menuAdapter: MenuAdapter by lazy {
        MenuAdapter()
    }

    private val vpAdapter: OrderVpAdapter by lazy {
        OrderVpAdapter(supportFragmentManager,lifecycle)
    }
    override fun setLayout(): Int {
        return R.layout.hp_activity_employees_attendance
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("考勤")
        initView()
    }

    override fun getPresenter(): HomePagePresenter {
       return HomePagePresenter()
    }

    private fun initView(){
        /**
         * 菜单处理
         */
        rv_menu.apply {
            layoutManager= LinearLayoutManager(this@EmployeesAttendanceActivity).apply {
                orientation= LinearLayoutManager.HORIZONTAL
            }
            adapter=menuAdapter
        }
        val listMenu=ArrayList<CategoryResultBean>()
        val listFragment=ArrayList<Fragment>()
        for (index in 1 until 12){
            listMenu.add(menuItem("$index",index))
            val fragment=AttendanceFragment.getInstance(2022,index)
            fragment?.apply {
                listFragment.add(this)
            }

        }
        menuAdapter.setNewInstance(listMenu)
        menuAdapter.setOnItemClickListener { _, _, position -> vp_date.currentItem=position }



        /**
         * viewpager处理
         */
        vp_date?.apply {
            adapter=vpAdapter
        }
        vp_date?.offscreenPageLimit=listMenu.size
        vp_date?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                menuAdapter.setSelectItem(position)
            }
        })
        vpAdapter.setNewData(listFragment)
    }
    private fun menuItem(name:String,id:Int):CategoryResultBean{
        return CategoryResultBean(id,name,0,"","")
    }

}