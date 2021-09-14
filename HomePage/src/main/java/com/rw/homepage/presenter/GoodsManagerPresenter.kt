package com.rw.homepage.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.homepage.HttpApi
import com.rw.homepage.bean.CategoryBean
import com.rw.homepage.bean.ReqAddCategory
import com.rw.service.ServiceViewModule

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
                HttpApi.HTTP_GET_CATEGORY_LIST, CategoryBean::class.java, true,
                mapOf("token" to bean.token)
            )
        }

    }


    /**
     * 添加品类
     */
    fun addCategory(category: ReqAddCategory) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                1,
                HttpApi.HTTP_ADD_CATEGORY, BaseBean::class.java, true,
                mapOf("token" to bean.token),category
            )
        }

    }
}