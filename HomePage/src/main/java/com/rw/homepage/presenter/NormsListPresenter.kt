package com.rw.homepage.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.homepage.HttpApi
import com.rw.homepage.bean.*
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2021/12/4.
 * Desc:
 */
class NormsListPresenter :HomePagePresenter() {
    /**
     * 获取规格属性列表
     */
    fun getNormsList(req: NormsListReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_NORMS_LIST, NormsItemBean::class.java, true,
                linkedMapOf("token" to bean.token), req
            )
        }

    }

    /**
     * 添加规格
     */
    fun addNorms(req: AddNormsReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_ADD_NORMS, NormsResultBean::class.java, true,
                linkedMapOf("token" to bean.token), req
            )
        }

    }


    /**
     * 添加规格属性
     */
    fun addNormsAttribute(req: AddNormsAttribute) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_ADD_ATTRIBUTE, NormsAttributeResultBean::class.java, true,
                linkedMapOf("token" to bean.token), req
            )
        }

    }

    /**
     * 删除规格属性
     */
    fun deleteNorms(req: DeleteNormsReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_DELETE_NORMS, BaseBean::class.java, true,
                linkedMapOf("token" to bean.token), req
            )
        }

    }

    /**
     * 删除规格属性
     */
    fun deleteNormsAttribute(req: DeleteNormsReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_DELETE_ATTRIBUTE, BaseBean::class.java, true,
                linkedMapOf("token" to bean.token), req
            )
        }

    }
}