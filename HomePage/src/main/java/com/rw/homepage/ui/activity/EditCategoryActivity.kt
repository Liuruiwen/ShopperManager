package com.rw.homepage.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.CategoryListAdapter
import com.rw.homepage.bean.CategoryBean
import com.rw.homepage.presenter.GoodsManagerPresenter
import com.rw.personalcenter.until.setVisible
import kotlinx.android.synthetic.main.hp_activity_edit_category.*
import kotlinx.android.synthetic.main.hp_activity_goods_manager.*

class EditCategoryActivity : BaseActivity<GoodsManagerPresenter>() {

    private val mAdapter: CategoryListAdapter by lazy{
        CategoryListAdapter()
    }

    override fun setLayout(): Int {
       return R.layout.hp_activity_edit_category
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("品类管理")
        initView()
        request()
    }

    override fun getPresenter(): GoodsManagerPresenter {
        return GoodsManagerPresenter()
    }

    private fun initView(){
        rv_category.apply {
           layoutManager=LinearLayoutManager(this@EditCategoryActivity)
            adapter=mAdapter
        }
//        mAdapter.draggableModule.isDragEnabled=true
//        mAdapter.draggableModule.setOnItemDragListener(object : OnItemDragListener {
//            override fun onItemDragMoving(
//                source: RecyclerView.ViewHolder?,
//                from: Int,
//                target: RecyclerView.ViewHolder?,
//                to: Int
//            ) {
//
//            }
//
//            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
//
//            }
//
//            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
//                showToast("${mAdapter.getItem(pos).categoryName}")
//            }
//
//        })
        mAdapter.apply {
            draggableModule.isDragEnabled=true
            draggableModule.setOnItemDragListener(object : OnItemDragListener {
                override fun onItemDragMoving(
                    source: RecyclerView.ViewHolder?,
                    from: Int,
                    target: RecyclerView.ViewHolder?,
                    to: Int
                ) {

                }

                override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

                }

                override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                    showToast("${mAdapter.getItem(pos).categoryName}")
                }

            })
            setOnItemClickListener { _, _, position ->  showToast("${mAdapter.getItem(position).categoryName}")}
        }
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_CATEGORY_LIST -> {
                    if (it is CategoryBean){
                          mAdapter.setNewInstance(it.data)
                    }


//                    tvRight?.text=if (!data.data.isNullOrEmpty()) "管理" else "添加品类"
                }
                HttpApi.HTTP_ADD_CATEGORY -> {
                    mPresenter?.reqCategory()
                }
                else -> showToast("系统异常")
            }
        })
    }

    private fun request(){
           mPresenter?.reqCategory()
    }

}