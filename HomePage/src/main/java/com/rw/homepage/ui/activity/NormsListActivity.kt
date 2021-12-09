package com.rw.homepage.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.until.ViewHolder
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.NormsListAdapter
import com.rw.homepage.adapter.TYPE_NORMS_ITEM_ATTRIBUTE
import com.rw.homepage.adapter.TYPE_NORMS_ITEM_HEADER
import com.rw.homepage.bean.*
import com.rw.homepage.presenter.NormsListPresenter
import com.rw.homepage.ui.dialog.AddCategoryDialog
import com.rw.personalcenter.until.setVisible
import kotlinx.android.synthetic.main.hp_norms_list.*
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast

class NormsListActivity : BaseActivity<NormsListPresenter>() {
    private val listNorms = ArrayList<MultiItemBean>()
    private var categoryId:String?=null
    private var insertPosition=-1
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
            setOnItemClickListener { _, _, position ->  mAdapter.attributeClick(position)}
            setOnItemChildClickListener { _, view, position -> childItemClick(view,position) }
            addChildClickViewIds(R.id.tv_delete)
            addChildClickViewIds(R.id.tv_add)
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
                HttpApi.HTTP_ADD_ATTRIBUTE->{
                           if (it is NormsAttributeResultBean){
                               it.data?.let {attributeBean->

                                   if (insertPosition>-1){
                                       insertPosition+=1
                                       attributeBean.itemType= TYPE_NORMS_ITEM_ATTRIBUTE
                                       mAdapter.addAttribute(attributeBean,insertPosition)
                                       insertPosition=-1
                                   }

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

    private fun childItemClick(view: View,position:Int){
        val item=mAdapter.getItem(position)

        when(view.id){
            R.id.tv_delete->{

            }
            R.id.tv_add->{

                if (item is NormsHeaderBean){
                    insertPosition=position
                    addNormsAttribute(item.id.toString())
                }

            }

        }
    }


    fun addNormsAttribute(id:String?){

        object : AddCategoryDialog(this){
            override fun helper(helper: ViewHolder?) {
                super.helper(helper)
                val etName=helper?.getView<EditText>(R.id.et_name)
                etName?.setVisible(false)
                val etDesc=helper?.getView<EditText>(R.id.et_desc)
                 etDesc?.hint="请输入属性名称"
                helper?.setOnClickListener(View.OnClickListener {

                    when(it?.id){
                        R.id.tv_cancel->{
                            dismiss()
                        }
                        R.id.tv_confirm->{

                            val desc=etDesc?.text.toString().trim()
                            if (desc.isEmpty()){
                                context .toast("请输入属性名称")
                                return@OnClickListener
                            }
                            mPresenter?.addNormsAttribute(AddNormsAttribute(desc,id?:""))
                            dismiss()
                        }
                    }
                }, R.id.tv_cancel, R.id.tv_confirm)
            }
        }.show()

    }

}