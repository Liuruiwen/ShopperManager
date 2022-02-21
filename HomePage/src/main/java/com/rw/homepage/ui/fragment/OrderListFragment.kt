package com.rw.homepage.ui.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseFragment
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.OrderListAdapter
import com.rw.homepage.bean.OrderListResultBean
import com.rw.homepage.bean.ShopperResultBean
import com.rw.homepage.presenter.OrderListPresenter
import kotlinx.android.synthetic.main.hp_fragment_order_list.*

/**
 * Created by Amuse
 * Date:2022/2/9.
 * Desc:
 */
class OrderListFragment : BaseFragment<OrderListPresenter>(){
    private var pageNum=1
    private val mAdapter:OrderListAdapter by lazy {
        OrderListAdapter()
    }
    override fun getViewLayout(): Int {
       return R.layout.hp_fragment_order_list
    }

    override fun lazyData() {
        super.lazyData()
        mPresenter?.getOrderList(OrderListPresenter.OrderListReq(pageNum,10))
    }
    override fun initView() {
        rv_order?.apply {
            layoutManager=LinearLayoutManager(mContext)
            adapter=mAdapter
        }
    }

    override fun loadData() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_ORDER_LIST -> {
                    if (it is OrderListResultBean){
                        mAdapter.setNewInstance(it.data?.data)
                    }


                }
                else -> showToast("系统异常")
            }
        })
    }

    override fun getPresenter(): OrderListPresenter {
        return OrderListPresenter()
    }


}