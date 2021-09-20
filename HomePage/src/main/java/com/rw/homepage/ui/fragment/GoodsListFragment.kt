package com.rw.homepage.ui.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseFragment
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.GoodsListAdapter
import com.rw.homepage.bean.CategoryBean
import com.rw.homepage.bean.GoodsBean
import com.rw.homepage.presenter.GoodsListPresenter
import com.rw.personalcenter.until.setVisible
import kotlinx.android.synthetic.main.hp_activity_goods_manager.*
import kotlinx.android.synthetic.main.hp_fragment_goods_list.*

/**
 * Created by Amuse
 * Date:2021/9/15.
 * Desc:商品列表
 */
class GoodsListFragment :BaseFragment<GoodsListPresenter>(){
    private val mAdapter: GoodsListAdapter by lazy {
        GoodsListAdapter()
    }
    override fun getViewLayout(): Int {
     return R.layout.hp_fragment_goods_list
    }

    override fun initView() {
        rv_goods.apply {
            layoutManager=LinearLayoutManager(mContext)
            adapter=mAdapter
        }

    }

    override fun loadData() {
        reqResult()
    }

    override fun getPresenter(): GoodsListPresenter {
      return  GoodsListPresenter()
    }

    override fun lazyData() {
        super.lazyData()
        arguments?.apply {
            val id= getInt("id",0)
            mPresenter?.reqGoodsList(id)
        }

    }

    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_GOODS_LIST -> {
                    val data=it as GoodsBean
                    layout_empty.setVisible(data.data.isNullOrEmpty())
                    rv_menu.setVisible(!data.data.isNullOrEmpty())
                    mAdapter.setNewInstance(it.data)
//                    tvRight?.text=if (!data.data.isNullOrEmpty()) "管理" else "添加品类"
                }
                else -> showToast("系统异常")
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let { bean ->
                bean.message?.let { message ->
                    showToast(message)
                }

            }

        })
    }
}