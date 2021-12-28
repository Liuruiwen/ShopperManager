package com.rw.homepage.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.GoodsEditNormsAdapter
import com.rw.homepage.adapter.SpinnerAdapter
import com.rw.homepage.bean.*
import com.rw.homepage.model.GoodsEditModel
import com.rw.homepage.presenter.GoodsEditPresenter
import com.rw.homepage.until.setVisible
import kotlinx.android.synthetic.main.hp_activity_goods_edit.*
import kotlinx.android.synthetic.main.hp_activity_goods_edit.tv_add_norms
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor

const val GOODS_EDIT_TYPE_ADD = 1//增加商品
const val GOODS_EDIT_TYPE_EDIT = 2//编辑商品

class GoodsEditActivity : BaseActivity<GoodsEditPresenter>() {

    private var spinnerType=1
    private val categroyId: Int by lazy {
        intent.getIntExtra("id", 0)
    }
    private var tvRight: TextView?=null
//    registerForActivityResult

//    private val requestDataLauncher = registerFor(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == RESULT_OK) {
//            val data = result.data?.getStringExtra("data")
//            // Handle data from SecondActivity
//        }
//    }

    private val mAdapter:GoodsEditNormsAdapter by lazy {
        GoodsEditNormsAdapter()
    }

    override fun setLayout(): Int {
        return R.layout.hp_activity_goods_edit
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        val type = intent.getIntExtra("type", GOODS_EDIT_TYPE_ADD)

        titleView.setTitle(if (type == GOODS_EDIT_TYPE_ADD) "添加商品" else "商品编辑")
        tvRight = titleView.getView(R.id.tv_title_right)
        tvRight?.setVisible(true)
        tvRight?.text = "完成"
        tvRight?.textColor= ContextCompat.getColor(this,R.color.colorPrimary)
        tvRight?.setOnClickListener {
             if (isCommit()){
//                 "[{\"normsId\":1,\"list\":[1,2,3]},{\"normsId\":2,\"list\":[6,7,8,9,10,11,12]}]",
               mPresenter?.reqAddGoods(AddGoodsReq(categroyId,
                   et_price.text.toString().trim(),
                   et_name.text.toString().trim(),
                   et_desc.text.toString().trim(),
                   "",
                   mAdapter.getNormsId()?:"",
                   spinnerType
               ))
             }
        }
        tv_add_norms.setOnClickListener {
            startActivity<NormsListActivity>("categoryId" to categroyId.toString())
        }
        rv_norms_list.apply {
            layoutManager = GridLayoutManager(this@GoodsEditActivity, 6)
            adapter = mAdapter
        }
        mAdapter.apply {
            setGridSpanSizeLookup { _, _, position ->  mAdapter.data[position].getSpanSize() }
        }


        GoodsEditModel.get()?.normsList?.observe(this, Observer {list->
             if (!list.isNullOrEmpty()){
                 processNormsList(list)
                 tv_add_norms.text=getString(R.string.hp_add_norms)
             }else{
                 tv_add_norms.text=getString(R.string.hp_edit_norms)
             }
        })

        processSpinner()
        reqResult()
    }

    override fun getPresenter(): GoodsEditPresenter {
        return GoodsEditPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        GoodsEditModel.destroy()
    }
    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_ADD_GOODS -> {
                    showToast("添加商品成功")
                    finish()
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
     * 完成处理
     */
    private fun isCommit():Boolean{
        val tvPrice=et_price.text.toString().trim()
        if (tvPrice.isEmpty()){
            showToast(getString(R.string.hp_input_goods_price))
            return false
        }
        val tvName=et_name.text.toString().trim()
        if (tvName.isEmpty()){
            showToast(getString(R.string.hp_input_goods_name))
            return false
        }
        val tvDesc=et_desc.text.toString().trim()
        if (tvDesc.isEmpty()){
            showToast(getString(R.string.hp_input_goods_desc))
            return false
        }


        return true
    }

    /**
     * 上下架处理
     */
    private fun processSpinner(){
        val list=arrayListOf(SpinnerBean(1,"上架"),SpinnerBean(2,"下架"))
        val spinnerAdapter= SpinnerAdapter(this, list)
        spinner.adapter=spinnerAdapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerType = list[position].type
            }

        }
    }

    /**
     * 处理规格数据
     */
    private fun processNormsList(list:List<MultiItemBean>){
        mAdapter.data.clear()
        val listNorms=ArrayList<MultiItemBean>()
        var itemHeader:NormsHeaderBean?=null
        for (index in list.indices){
            val item=list[index]
            if (item is NormsHeaderBean){
                itemHeader=item
            }else if (item is NormsAttributeBean && item.selectType==1){
                    if (itemHeader!=null){
                        listNorms.add(itemHeader)
                        itemHeader=null
                    }
                    listNorms.add(item)
                }


        }
        mAdapter.setNewInstance(listNorms)
    }
}