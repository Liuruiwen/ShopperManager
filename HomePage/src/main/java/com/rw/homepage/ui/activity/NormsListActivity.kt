package com.rw.homepage.ui.activity

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
import com.rw.homepage.model.GoodsEditModel
import com.rw.homepage.presenter.NormsListPresenter
import com.rw.homepage.ui.dialog.AddCategoryDialog
import com.rw.homepage.ui.dialog.MessageDialog
import com.rw.homepage.until.setVisible
import kotlinx.android.synthetic.main.hp_norms_list.*
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast

class NormsListActivity : BaseActivity<NormsListPresenter>() {
//    private val listNorms = ArrayList<MultiItemBean>()
    private var categoryId:String?=null
    private var insertPosition=-1
    private var deletePosition=-1
    private var deleteType=1
    override fun setLayout(): Int {
        return R.layout.hp_norms_list
    }
    private var mMessageDialog:MessageDialog?=null

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
             if (mAdapter.isSelectAttribute()){
                 GoodsEditModel.get()?.normsList?.value=mAdapter.data
                 finish()
             }
        }
        initView()
        reqResult()
    }


    override fun getPresenter(): NormsListPresenter {
        return NormsListPresenter()
    }

    private fun initView() {
      mMessageDialog=  mPresenter?.showMessageDialog(this,
          View.OnClickListener {

              when(it?.id){
                  R.id.tv_cancel->{
                      mMessageDialog?.dismiss()
                  }
                  R.id.tv_confirm->{
                      if (deletePosition!=-1){
                          val item=mAdapter.getItem(deletePosition)
                          if (deleteType==1 && item is NormsAttributeBean){
                              mPresenter?.deleteNormsAttribute(DeleteNormsReq(item.id))
                          }else if (item is NormsHeaderBean){
                              mPresenter?.deleteNorms(DeleteNormsReq(item.id))
                          }
                      }
                      mMessageDialog?.dismiss()
                  }
              }
          },R.id.tv_cancel,R.id.tv_confirm)
        categoryId=intent.getStringExtra("categoryId")
        tv_add_norms.setOnClickListener {
            addNormsAttribute(categoryId,"请输入规格名称",2)
        }
        rv_norms.apply {
            layoutManager = GridLayoutManager(this@NormsListActivity, 6)
            adapter = mAdapter
        }
        mAdapter.apply {
            setGridSpanSizeLookup { _, _, position ->  mAdapter.data[position].getSpanSize() }
            setOnItemClickListener { _, _, position ->  mAdapter.attributeClick(position)}
            setOnItemChildClickListener { _, view, position -> childItemClick(view,position) }
            setOnItemLongClickListener { _, _, position ->  longItemClick(position)}
            addChildClickViewIds(R.id.tv_delete)
            addChildClickViewIds(R.id.tv_add)
            addChildLongClickViewIds()
        }

    }
    private fun reqResult() {
        val listNorms=GoodsEditModel.get()?.normsList?.value
        if (!listNorms.isNullOrEmpty()){
            mAdapter.setNewInstance(listNorms)
        }else{
            mPresenter?.getNormsList(NormsListReq(categoryId?:"2"))
        }

        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_NORMS_LIST -> {
                   if (it is NormsItemBean){
                       if (!it.data.isNullOrEmpty()){
                           mAdapter.data.clear()
                           val listNorms=ArrayList<MultiItemBean>()
                           it.data?.forEach {hearBean->
                               hearBean.itemType= TYPE_NORMS_ITEM_HEADER
                               listNorms.add(hearBean)
                               hearBean.listAttribute?.forEach{attributeBean->
                                   attributeBean.itemType= TYPE_NORMS_ITEM_ATTRIBUTE
                                   listNorms.add(attributeBean)
                               }
                           }
                           mAdapter.setNewInstance(listNorms)
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
                HttpApi.HTTP_DELETE_NORMS->{//删除规格
                    if (deletePosition!=-1){
                        mPresenter?.getNormsList(NormsListReq(categoryId?:"2"))
                    }

                }
                HttpApi.HTTP_ADD_NORMS->{//添加规格
                    if (it is NormsResultBean){
                        it.data?.let {normsBean->

                            normsBean.itemType= TYPE_NORMS_ITEM_HEADER
                            mAdapter.addAttribute(normsBean,0)

                        }
                    }
                }
                HttpApi.HTTP_DELETE_ATTRIBUTE->{//删除规格属性
                    if (deletePosition!=-1){
                        mAdapter.remove(mAdapter.getItem(deletePosition))
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
                if (item is NormsHeaderBean){
                    deleteType=0
                    deletePosition=position
                    mMessageDialog?.showDialog("确定删除${item.normsName}商品规格？")
                }
            }
            R.id.tv_add->{

                if (item is NormsHeaderBean){
                    insertPosition=position
                    addNormsAttribute(item.id.toString(),"请输入属性名称",1)
                }

            }

        }
    }

    private fun longItemClick(position: Int):Boolean{
        val item=mAdapter.getItem(position)
        if (item is NormsAttributeBean){
            deletePosition=position
            deleteType=1
            mMessageDialog?.showDialog("确定删除${item.normsAttributeName}规格属性？")
        }

        return true
    }


    private fun addNormsAttribute(id:String?,hint:String,type:Int){

        object : AddCategoryDialog(this){
            override fun helper(helper: ViewHolder?) {
                super.helper(helper)
                val etName=helper?.getView<EditText>(R.id.et_name)
                etName?.setVisible(false)
                val etDesc=helper?.getView<EditText>(R.id.et_desc)
                 etDesc?.hint=hint
                helper?.setOnClickListener(View.OnClickListener {

                    when(it?.id){
                        R.id.tv_cancel->{
                            dismiss()
                        }
                        R.id.tv_confirm->{

                            val desc=etDesc?.text.toString().trim()
                            if (desc.isEmpty()){
                                context .toast(hint)
                                return@OnClickListener
                            }
                            if (type==1){//添加属性
                                mPresenter?.addNormsAttribute(AddNormsAttribute(desc,id?:""))
                            }else{//添加规格
                                mPresenter?.addNorms(AddNormsReq(desc,id?:""))
                            }

                            dismiss()
                        }
                    }
                }, R.id.tv_cancel, R.id.tv_confirm)
            }
        }.show()

    }

}