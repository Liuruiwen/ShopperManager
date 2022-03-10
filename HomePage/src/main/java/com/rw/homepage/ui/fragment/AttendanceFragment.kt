package com.rw.homepage.ui.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rw.basemvp.BaseFragment
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.AttendanceAdapter
import com.rw.homepage.bean.AttendanceResultBean
import com.rw.homepage.bean.OrderListResultBean
import com.rw.homepage.presenter.AttendancePresenter
import kotlinx.android.synthetic.main.hp_fragment_attendance.*
import kotlinx.android.synthetic.main.hp_fragment_order_list.*

/**
 * Created by Amuse
 * Date:2022/3/10.
 * Desc:
 */
class AttendanceFragment  : BaseFragment<AttendancePresenter>(){
    private val mAdapter : AttendanceAdapter by lazy {
        AttendanceAdapter()
    }

    override fun lazyData() {
        super.lazyData()
        mPresenter?.getAttendance(AttendancePresenter.AttendanceReq("2022-3"))
    }

    override fun getViewLayout(): Int {
        return R.layout.hp_fragment_attendance
    }

    override fun initView() {
        rv_attendance?.apply {
            layoutManager=GridLayoutManager(context,7)
            adapter=mAdapter
        }
    }

    override fun loadData() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_ATTENDANCE -> {
                    refreshLayout.finishRefresh()
                    if (it is AttendanceResultBean) {
                        if (!it.data.isNullOrEmpty()){

                        }

                    }

                }
                else -> showToast("系统异常")
            }
        })
    }

    override fun getPresenter(): AttendancePresenter {
        return AttendancePresenter()
    }
}