package com.rw.homepage.presenter

import android.content.Context
import android.view.View
import android.widget.EditText
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.until.ViewHolder
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.bean.*
import com.rw.homepage.ui.activity.GoodsEditActivity
import com.rw.homepage.ui.dialog.AddCategoryDialog
import com.rw.service.ServiceViewModule
import org.jetbrains.anko.toast

/**
 * Created by Amuse
 * Date:2021/9/14.
 * Desc:
 */
class GoodsManagerPresenter : HomePagePresenter() {
    /**
     * 获取品类信息
     */
    fun reqCategory() {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_CATEGORY_LIST, CategoryListBean::class.java, true,
                mapOf("token" to bean.token)
            )
        }

    }


    /**
     * 添加品类
     */
    fun addCategory(category: AddCategoryReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                1,
                HttpApi.HTTP_ADD_CATEGORY, CategoryBean::class.java, true,
                mapOf("token" to bean.token),category
            )
        }

    }

    /**
     * 删除品类
     */
    fun deleteCategory(category: GoodsListReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                2,
                HttpApi.HTTP_DELETE_CATEGORY, BaseBean::class.java, true,
                mapOf("token" to bean.token),category
            )
        }

    }

    /**
     * 编辑品类
     */
    fun editCategory(category: EditCategoryReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                3,
                HttpApi.HTTP_EDIT_CATEGORY, EditCategoryResultBean::class.java, true,
                mapOf("token" to bean.token),category
            )
        }

    }


     fun showAddCategory(context: Context,categoryReq:CategoryResultBean?=null){

        object : AddCategoryDialog(context){
            override fun helper(helper: ViewHolder?) {
                super.helper(helper)
                val etName=helper?.getView<EditText>(R.id.et_name)
                val etDesc=helper?.getView<EditText>(R.id.et_desc)
                val etPosition=helper?.getView<EditText>(R.id.et_position)
                categoryReq?.let { req->
                    etName?.setText(req.categoryName)
                    etDesc?.setText(req.categoryDesc)
                }
                helper?.setOnClickListener(View.OnClickListener {

                    when(it?.id){
                        R.id.tv_cancel->{
                            dismiss()
                        }
                        R.id.tv_confirm->{
                            val name=etName?.text.toString().trim()
                            if (name.isEmpty()){
                                context .toast(context.getString(R.string.hp_input_category_name))
                                return@OnClickListener
                            }
                            val desc=etDesc?.text.toString().trim()
                            if (desc.isEmpty()){
                                context .toast(context.getString(R.string.hp_input_category_desc))
                                return@OnClickListener
                            }
//                            val position=etPosition?.text.toString().trim()
//                            if (position.isEmpty()){
//                                context.toast(context.getString(R.string.hp_input_position))
//                                return@OnClickListener
//                            }
                            if (categoryReq!=null){
                                editCategory(EditCategoryReq(categoryReq.id,name,desc))
                            }else{
                                addCategory(AddCategoryReq(name,desc))
                            }

                            dismiss()
                        }
                    }
                }, R.id.tv_cancel, R.id.tv_confirm)
            }
        }.show()

    }
}