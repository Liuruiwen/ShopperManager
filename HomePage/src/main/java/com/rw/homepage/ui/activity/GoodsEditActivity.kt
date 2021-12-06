package com.rw.homepage.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.SpinnerAdapter
import com.rw.homepage.bean.AddGoodsReq
import com.rw.homepage.bean.SpinnerBean
import com.rw.homepage.presenter.GoodsEditPresenter
import com.rw.personalcenter.until.setVisible
import kotlinx.android.synthetic.main.hp_activity_goods_edit.*
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
               mPresenter?.reqAddGoods(AddGoodsReq(categroyId,
                   et_price.text.toString().trim(),
                   et_name.text.toString().trim(),
                   et_desc.text.toString().trim(),
                   "",
                      "[{\"normsId\":1,\"list\":[1,2,3]},{\"normsId\":2,\"list\":[6,7,8,9,10,11,12]}]",
                   spinnerType
               ))
             }
        }
        tv_add_norms.setOnClickListener {
            startActivity<NormsListActivity>("categoryId" to categroyId.toString())
        }
        processSpinner()
        reqResult()
    }

    override fun getPresenter(): GoodsEditPresenter {
        return GoodsEditPresenter()
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
}