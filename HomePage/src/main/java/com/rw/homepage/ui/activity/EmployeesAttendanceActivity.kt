package com.rw.homepage.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.until.ViewHolder
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.R
import com.rw.homepage.adapter.MenuAdapter
import com.rw.homepage.adapter.OrderVpAdapter
import com.rw.homepage.adapter.SelectYearAdapter
import com.rw.homepage.bean.CategoryResultBean
import com.rw.homepage.presenter.HomePagePresenter
import com.rw.homepage.ui.dialog.SelectYearDialog
import com.rw.homepage.ui.fragment.AttendanceFragment
import kotlinx.android.synthetic.main.hp_activity_employees_attendance.*
import kotlinx.android.synthetic.main.hp_activity_goods_manager.rv_menu
import java.util.*
import kotlin.collections.ArrayList


class EmployeesAttendanceActivity : BaseActivity<HomePagePresenter>() {

    private var selectYear:Int=2022
    private var tvYear:TextView?=null
    private val menuAdapter: MenuAdapter by lazy {
        MenuAdapter()
    }

    private var attendanceFragment: AttendanceFragment?=null
    private  var listFragment:ArrayList<AttendanceFragment>?=null

    private val vpAdapter: OrderVpAdapter<AttendanceFragment> by lazy {
        OrderVpAdapter<AttendanceFragment>(supportFragmentManager,lifecycle)
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
        tvYear=findViewById(R.id.tv_year)
        selectYear=Calendar.getInstance().get(Calendar.YEAR)
      val  currentMonth=Calendar.getInstance().get(Calendar.MONTH)

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
         listFragment=ArrayList<AttendanceFragment>()
        for (index in 1 until 13){
            listMenu.add(menuItem("${index}月",index))
            val fragment=AttendanceFragment.getInstance(selectYear,index)
            if (fragment is AttendanceFragment){
                listFragment?.add(fragment)
            }

        }
        menuAdapter.setNewInstance(listMenu)
        menuAdapter.setOnItemClickListener { _, _, position -> vp_date.currentItem=position }


        tvYear?.text=selectYear.toString()


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
                setSelectDate(position)

            }
        })
        vpAdapter.setNewData(listFragment)
        vp_date.currentItem=currentMonth
        tvYear?.setOnClickListener {

                object : SelectYearDialog(this){
                    override fun helper(helper: ViewHolder?) {
                        super.helper(helper)

                        val viewFlipper=helper?.getView<RecyclerView>(R.id.rv_year)
                        val yearAdapter= SelectYearAdapter()
                        viewFlipper?.apply {
                            layoutManager=LinearLayoutManager(context)
                            adapter=yearAdapter
                        }
                        val datas= arrayListOf("2020","2021","2022","2023","2024","2025","2026","2027")
                        yearAdapter.setNewInstance(datas)
                        yearAdapter.setSelectItem(selectYear.toString())
                        viewFlipper?.scrollToPosition(datas.indexOf(selectYear.toString()))
                        yearAdapter.setOnItemClickListener(object : OnItemClickListener{
                            override fun onItemClick(p0: BaseQuickAdapter<*, *>, p1: View, position: Int) {
                                val item=yearAdapter.getItem(position)
                                tvYear?.text=item
                                selectYear=item.toInt()
                                yearAdapter.setSelectItem(item)
                                attendanceFragment?.refreshData(selectYear)
                                dismiss()
                            }

                        })


                    }
                }.show()


        }
    }
    private fun menuItem(name:String,id:Int):CategoryResultBean{
        return CategoryResultBean(id,name,0,"","")
    }


    /**
     * 设置选中事件
     */
    private fun setSelectDate(position:Int){
        menuAdapter.setSelectItem(position)
        rv_menu.scrollToPosition(position)
        attendanceFragment=listFragment?.get(position)
    }


}