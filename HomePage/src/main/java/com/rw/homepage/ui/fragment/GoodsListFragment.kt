package com.rw.homepage.ui.fragment

import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rw.basemvp.BaseFragment
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.GoodsListAdapter
import com.rw.homepage.bean.GoodsBean
import com.rw.homepage.bean.GoodsListBean
import com.rw.homepage.presenter.GoodsListPresenter
import com.rw.homepage.ui.activity.EditCategoryActivity
import com.rw.homepage.ui.activity.GOODS_EDIT_TYPE_ADD
import com.rw.homepage.ui.activity.GOODS_EDIT_TYPE_EDIT
import com.rw.homepage.ui.activity.GoodsEditActivity
import com.rw.homepage.until.setVisible
import kotlinx.android.synthetic.main.hp_empty_state.*
import kotlinx.android.synthetic.main.hp_fragment_goods_list.*
import org.jetbrains.anko.startActivity

/**
 * Created by Amuse
 * Date:2021/9/15.
 * Desc:商品列表
 */
class GoodsListFragment :BaseFragment<GoodsListPresenter>(){
    private var categoryId=0
    private var selectPosition=-1
    private var itemPosition=-1
    private val mAdapter: GoodsListAdapter by lazy {
        GoodsListAdapter()
    }
    private var result=registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        when(it.resultCode){
            2001, 2002->{
                if (!it.data?.getStringExtra("goods").isNullOrEmpty()){
                    val item=Gson().fromJson<GoodsListBean>(it.data?.getStringExtra("goods"), object : TypeToken<GoodsListBean>() {}.type)
                    item?.let { bean->
                        if (it.resultCode==2002){
                            mAdapter.addData(0,bean)
                            setDataState(true)

                        }else{
                            mAdapter.updateItem(itemPosition,bean)
                        }
                        itemPosition=-1
                    }
                }
            }
        }
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
        mAdapter.setOnItemChildClickListener { _, view, position ->  childClick(view,position) }
        mAdapter.addChildClickViewIds(R.id.tv_shelves)
        mAdapter.addChildClickViewIds(R.id.tv_delete)
        mAdapter. addChildClickViewIds(R.id.tv_show)
        mAdapter.setOnItemClickListener { _, _, position ->itemClick(position) }
        tv_add?.setOnClickListener {view->
            val it=Intent(mContext, GoodsEditActivity::class.java)
            it.apply {
                putExtra("type", GOODS_EDIT_TYPE_ADD)
                putExtra("id", categoryId)
            }
            result.launch(it)

        }
        tv_add_goods?.setOnClickListener {
            tv_add?.performClick()
        }
    }

    override fun getPresenter(): GoodsListPresenter {
      return  GoodsListPresenter()
    }

    override fun lazyData() {
        super.lazyData()
        arguments?.apply {
            categoryId= getInt("id",0)
            mPresenter?.reqGoodsList(categoryId)
        }

    }

    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_GOODS_LIST -> {
                    val data=it as GoodsBean
                    setDataState(!data.data.isNullOrEmpty())
                    mAdapter.setNewInstance(it.data)
                    tv_add?.text="添加商品"
                }
                HttpApi.HTTP_DELETE_GOODS->{
                    if (selectPosition>-1){
                        mAdapter.remove(mAdapter.getItem(selectPosition))
                        selectPosition=-1
                        setDataState(!mAdapter.data.isNullOrEmpty())
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

    private fun childClick(view : View, position:Int){
        selectPosition=position
        when(view.id){
            R.id.tv_shelves->{//上下架

            }
            R.id.tv_delete->{//删除
                mPresenter?.reqDeleteGoods(mAdapter.getItem(position).id)
            }
            R.id.tv_show->{//展开、收起
              mAdapter.updateShow(position)
            }
        }
    }

    private fun itemClick(position: Int){
        itemPosition=position
        val item=mAdapter.getItem(position)
        val data=Gson().toJson(item)
        val it=Intent(mContext, GoodsEditActivity::class.java)
        it.apply {
            putExtra("type", GOODS_EDIT_TYPE_EDIT)
            putExtra("id", categoryId)
            putExtra( "goodsItem", data)
            putExtra("goodsId", item.id)
        }
        result.launch(it)

    }

    private fun setDataState(isTrue:Boolean){
        tv_add_goods?.setVisible(isTrue)
        tv_goods_position?.setVisible(isTrue)
        goods_empty?.setVisible(!isTrue)
        rv_goods?.setVisible(isTrue)
    }
}