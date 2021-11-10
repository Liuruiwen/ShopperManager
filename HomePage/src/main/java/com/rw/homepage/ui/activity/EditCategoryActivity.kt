package com.rw.homepage.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import com.rw.homepage.bean.CategoryListBean
import com.rw.homepage.bean.ReqGoodsList
import com.rw.homepage.presenter.GoodsManagerPresenter
import com.rw.personalcenter.until.setVisible
import kotlinx.android.synthetic.main.hp_activity_edit_category.*
import kotlinx.android.synthetic.main.hp_activity_goods_manager.*

class EditCategoryActivity : BaseActivity<GoodsManagerPresenter>() {

    private var selectPosition=-1
    private val mAdapter: CategoryListAdapter by lazy{
        CategoryListAdapter()
    }

    override fun setLayout(): Int {
       return R.layout.hp_activity_edit_category
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("品类管理")
      val  tvRight=titleView.getView<TextView>(R.id.tv_title_right)
        tvRight?.setVisible(true)
        tvRight?.text="添加品类"
        tvRight?.setOnClickListener {
            mPresenter?.showAddCategory(this@EditCategoryActivity)
        }

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

                }

            })
            addChildClickViewIds(R.id.tv_edit)
            addChildClickViewIds(R.id.tv_delete)
            setOnItemChildClickListener { _, view, position -> childClick(position,view) }
        }
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_CATEGORY_LIST -> {
                    if (it is CategoryListBean){
                          mAdapter.setNewInstance(it.data)
                    }
                }
                HttpApi.HTTP_DELETE_CATEGORY -> {
                    if (selectPosition>-1){
                       mAdapter.remove(mAdapter.getItem(selectPosition))
                    }
                }
                HttpApi.HTTP_ADD_CATEGORY -> {
                    if (it is CategoryBean){
                        mAdapter.addData(it.data)
                    }

                }
                else -> showToast("系统异常")
            }
        })
    }

    private fun childClick(position:Int,view: View?){
        selectPosition=position
         when(view?.id){
             R.id.tv_edit->{//编辑

             }
             R.id.tv_delete->{//删除
                mPresenter?.deleteCategory(ReqGoodsList(mAdapter.getItem(selectPosition).id))
             }
         }

    }
    private fun request(){
           mPresenter?.reqCategory()
    }

}