package com.rw.homepage.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.CategoryListAdapter
import com.rw.homepage.bean.*
import com.rw.homepage.presenter.GoodsManagerPresenter
import com.rw.homepage.until.setVisible
import kotlinx.android.synthetic.main.hp_activity_edit_category.*

/**
 * 品类管理
 *
 */
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

    override fun onBackPressed() {
        if (processClose()){
            super.onBackPressed()
        }


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
                HttpApi.HTTP_UPDATE_POSITION->{
                    finish()
                }
                HttpApi.HTTP_EDIT_CATEGORY -> {
                    if (it is EditCategoryResultBean){
                        it.data?.let {req->
                            mAdapter.editItem(selectPosition,req)
                        }

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
                 mPresenter?.showAddCategory(this@EditCategoryActivity,mAdapter.getItem(selectPosition))
             }
             R.id.tv_delete->{//删除
                mPresenter?.deleteCategory(GoodsListReq(mAdapter.getItem(selectPosition).id))
             }
         }

    }
    private fun request(){
           mPresenter?.reqCategory()
    }


    /**
     * 处理排序
     */
    private fun processClose():Boolean{
        var isClose=true
        val dataSize=mAdapter.data.size-1
        for (index in mAdapter.data.indices ){
            val item=mAdapter.getItem(index)
            if (index!=item.categoryPosition){
                var position=0
                mAdapter.data.forEach{
                    it.categoryPosition=position
                    position++
                }
                isClose=false
                mPresenter?.updateCategoryPosition(mAdapter.data)
            }else if  (index==dataSize){
                isClose=true

                break
            }

        }
        return isClose
    }

    override fun beforeFinish() {
        if (processClose()){
            finish()
        }
    }

}