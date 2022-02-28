package com.rw.homepage.ui.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseFragment
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.OrderListAdapter
import com.rw.homepage.bean.OrderListResultBean
import com.rw.homepage.presenter.OrderListPresenter
import com.scwang.smart.refresh.header.ClassicsHeader
import kotlinx.android.synthetic.main.hp_fragment_order_list.*

/**
 * Created by Amuse
 * Date:2022/2/9.
 * Desc:
 */
class OrderListFragment : BaseFragment<OrderListPresenter>() {
    private var pageNum = 1
    private var isLoading:Boolean=false
    private val mAdapter: OrderListAdapter by lazy {
        OrderListAdapter()
    }

    override fun getViewLayout(): Int {
        return R.layout.hp_fragment_order_list
    }

    override fun lazyData() {
        super.lazyData()
        refreshLayout.autoRefresh()
    }

    override fun initView() {
        refreshLayout?.apply {
            setEnableLoadMoreWhenContentNotFull(false)
            setReboundDuration(100)
            setRefreshHeader(ClassicsHeader(mContext))
            setOnRefreshListener {
                onRefresh()
            }
        }
        mAdapter.apply {
            animationEnable=true
            loadMoreModule.isAutoLoadMore=true
            loadMoreModule.isEnableLoadMoreIfNotFullPage=false
            loadMoreModule.setOnLoadMoreListener {
                if (isLoading){
                    mPresenter?.getOrderList(OrderListPresenter.OrderListReq(pageNum, 10))
                }
            }
        }
        rv_order?.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = mAdapter
        }


    }

    override fun loadData() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_ORDER_LIST -> {
                    refreshLayout.finishRefresh()
                    if (it is OrderListResultBean) {
                        if (!it.data?.data.isNullOrEmpty()){
                            if (pageNum==1){
                                mAdapter.setNewInstance(it.data?.data)
                            }else{
                                it.data?.data?.let { data -> mAdapter.addData(data) }
                            }
                            pageNum++
                            isLoading=true
                            mAdapter.loadMoreModule.loadMoreComplete()
                        }else{
                            isLoading=false
                            mAdapter.loadMoreModule.loadMoreEnd()
                        }

                    }

                }
                else -> showToast("系统异常")
            }
        })
    }

    override fun getPresenter(): OrderListPresenter {
        return OrderListPresenter()
    }


    private fun onRefresh() {
        pageNum = 1
        mPresenter?.getOrderList(OrderListPresenter.OrderListReq(pageNum, 10))
    }



}