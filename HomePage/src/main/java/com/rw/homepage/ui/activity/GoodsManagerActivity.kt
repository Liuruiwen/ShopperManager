package com.rw.homepage.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.MenuAdapter
import com.rw.homepage.bean.CategoryBean
import com.rw.homepage.presenter.HomePagePresenter
import com.rw.personalcenter.until.setVisible
import com.rw.service.ServiceViewModule
import kotlinx.android.synthetic.main.hp_activity_goods_manager.*
import kotlinx.android.synthetic.main.hp_activity_goods_manager.layout_empty
import kotlinx.android.synthetic.main.hp_empty_state.*
import org.jetbrains.anko.toast

class GoodsManagerActivity : BaseActivity<HomePagePresenter>() {
   private val mAdapter: MenuAdapter by lazy {
       MenuAdapter()
   }

    override fun setLayout(): Int {
       return R.layout.hp_activity_goods_manager
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
       titleView.setTitle("商品管理")
        titleView.setVisible(R.id.tv_title_right, true)
        titleView.setText(R.id.tv_title_right, "管理")
        titleView.setChildClickListener(R.id.tv_title_right) {

        }
        initView()
        click()
        reqCategory()
        reqResult()
    }

    override fun getPresenter(): HomePagePresenter {
       return HomePagePresenter()
    }


    private fun initView(){

        rv_menu.apply {
            layoutManager=LinearLayoutManager(this@GoodsManagerActivity).apply {
                orientation=LinearLayoutManager.HORIZONTAL
            }
            adapter=mAdapter.apply {
                setOnItemClickListener { _, _, position -> setOnItem(position) }
            }
        }
    }


    private fun click(){
        tv_add.setOnClickListener {
           toast("让我说点什么好")
        }
    }

    private fun setOnItem(position:Int){
        mAdapter.setSelectItem(position)
    }

    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_CATEGORY_LIST -> {
                   val data=it as CategoryBean
                    layout_empty.setVisible(!data.data.isNullOrEmpty())
                    rv_menu.setVisible(!data.data.isNullOrEmpty())
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
    /**
     * 获取品类信息
     */
    private fun reqCategory() {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            mPresenter?.postBodyData(
                0,
                HttpApi.HTTP_GET_CATEGORY_LIST, CategoryBean::class.java, true,
                mapOf("token" to bean.token)
            )
        }

    }


}