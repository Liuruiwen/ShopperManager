package com.rw.homepage.ui.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.until.ViewHolder
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.MenuAdapter
import com.rw.homepage.bean.CategoryBean
import com.rw.homepage.bean.ReqAddCategory
import com.rw.homepage.presenter.GoodsManagerPresenter
import com.rw.homepage.ui.dialog.AddCategoryDialog
import com.rw.personalcenter.until.setVisible
import kotlinx.android.synthetic.main.hp_activity_goods_manager.*
import kotlinx.android.synthetic.main.hp_activity_goods_manager.layout_empty
import kotlinx.android.synthetic.main.hp_empty_state.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class GoodsManagerActivity : BaseActivity<GoodsManagerPresenter>() {
   private val mAdapter: MenuAdapter by lazy {
       MenuAdapter()
   }
    private var tvRight:TextView?=null

    override fun setLayout(): Int {
       return R.layout.hp_activity_goods_manager
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
       titleView.setTitle("商品管理")
        tvRight=titleView.getView(R.id.tv_title_right)
        tvRight?.setVisible(true)
        tvRight?.text="管理"
        tvRight?.setOnClickListener {
            showPw(it)
        }

        initView()
        click()
        mPresenter?.reqCategory()
        reqResult()
    }

    override fun getPresenter(): GoodsManagerPresenter {
       return GoodsManagerPresenter()
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
            showAddCategory()
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
                    layout_empty.setVisible(data.data.isNullOrEmpty())
                    rv_menu.setVisible(!data.data.isNullOrEmpty())
                    mAdapter.setNewInstance(it.data)
//                    tvRight?.text=if (!data.data.isNullOrEmpty()) "管理" else "添加品类"
                }
                HttpApi.HTTP_ADD_CATEGORY -> {
                    mPresenter?.reqCategory()
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


    private fun showAddCategory(){

            object : AddCategoryDialog(this){
                override fun helper(helper: ViewHolder?) {
                    super.helper(helper)
                    val etName=helper?.getView<EditText>(R.id.et_name)
                    val etDesc=helper?.getView<EditText>(R.id.et_desc)
                    val etPosition=helper?.getView<EditText>(R.id.et_position)

                    helper?.setOnClickListener(View.OnClickListener {

                        when(it?.id){
                            R.id.tv_cancel->{
                                dismiss()
                            }
                            R.id.tv_confirm->{
                                val name=etName?.text.toString().trim()
                                if (name.isEmpty()){
                                    toast(getString(R.string.hp_input_category_name))
                                    return@OnClickListener
                                }
                                val desc=etDesc?.text.toString().trim()
                                if (desc.isEmpty()){
                                    toast(getString(R.string.hp_input_category_desc))
                                    return@OnClickListener
                                }
                                val position=etPosition?.text.toString().trim()
                                if (position.isEmpty()){
                                    toast(getString(R.string.hp_input_position))
                                    return@OnClickListener
                                }

                                 mPresenter?.addCategory(ReqAddCategory(name,desc,position.toInt()))

                                dismiss()
                            }
                        }
                    }, R.id.tv_cancel, R.id.tv_confirm)
                }
            }.show()

    }

   private var popupWindow :PopupWindow?=null
    private fun showPw(tv:View){

        if (popupWindow==null){
            popupWindow =  PopupWindow(this)

            //设置宽高
            popupWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
// 获取布局
            val view= LayoutInflater.from(this.applicationContext).inflate(R.layout.hp_pw_manager, null)
            val tvAddCategory=view.findViewById<TextView>(R.id.tv_add_category)
            val tvManager=view.findViewById<TextView>(R.id.tv_manager_category)
            tvAddCategory?.setOnClickListener {
                showAddCategory()
                popupWindow?.dismiss()
            }
            tvManager?.setOnClickListener {
                startActivity<EditCategoryActivity>()
                popupWindow?.dismiss()
            }
            popupWindow?.contentView = view
// 点击区域外使对话框消失
            popupWindow?.isOutsideTouchable = false
            popupWindow?.isFocusable = true
            popupWindow?.isTouchable = true
            popupWindow?.setBackgroundDrawable( ColorDrawable(Color.WHITE))

        }
         val loc= arrayOf(0,1)
        val x=loc[0]
        val y=loc[1]
        popupWindow?.let {
           it.showAsDropDown(tv, it.width, 0)
        }



    }

}