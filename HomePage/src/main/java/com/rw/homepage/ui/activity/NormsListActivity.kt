package com.rw.homepage.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.NormsListAdapter
import com.rw.homepage.adapter.TYPE_NORMS_ITEM_ATTRIBUTE
import com.rw.homepage.adapter.TYPE_NORMS_ITEM_HEADER
import com.rw.homepage.bean.MultiItemBean
import com.rw.homepage.bean.NormsItemBean
import com.rw.homepage.bean.NormsListReq
import com.rw.homepage.presenter.NormsListPresenter
import com.rw.personalcenter.until.setVisible
import kotlinx.android.synthetic.main.hp_norms_list.*
import org.jetbrains.anko.textColor

class NormsListActivity : BaseActivity<NormsListPresenter>() {
    private val listNorms = ArrayList<MultiItemBean>()
    private var categoryId:String?=null
    override fun setLayout(): Int {
        return R.layout.hp_norms_list
    }

    private val mAdapter: NormsListAdapter by lazy {
        NormsListAdapter()
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("规格列表")
        val tvRight = titleView.getView<TextView>(R.id.tv_title_right)
        tvRight?.setVisible(true)
        tvRight?.textColor = ContextCompat.getColor(this, R.color.colorPrimary)
        tvRight?.text = "完成"
        tvRight?.setOnClickListener {

        }
        initView()
        reqResult()
    }


    override fun getPresenter(): NormsListPresenter {
        return NormsListPresenter()
    }

    private fun initView() {
        categoryId=intent.getStringExtra("categoryId")
        rv_norms.apply {
            layoutManager = GridLayoutManager(this@NormsListActivity, 6)
            adapter = mAdapter
        }
        mAdapter.apply {
            setGridSpanSizeLookup { _, _, position -> listNorms[position].getSpanSize() }
        }
    }
    private fun reqResult() {
        mPresenter?.getNormsList(NormsListReq(categoryId?:"2"))
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_NORMS_LIST -> {
                   if (it is NormsItemBean){
                       if (!it.data.isNullOrEmpty()){
                           it.data?.forEach {hearBean->
                               hearBean.itemType= TYPE_NORMS_ITEM_HEADER
                                   listNorms.add(hearBean)
                               hearBean.listAttribute?.forEach{attributeBean->
                                   attributeBean.itemType= TYPE_NORMS_ITEM_ATTRIBUTE
                                   listNorms.add(attributeBean)
                               }
                           }
                           mAdapter.setNewInstance(listNorms)
                       }else{

                       }

                   }
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