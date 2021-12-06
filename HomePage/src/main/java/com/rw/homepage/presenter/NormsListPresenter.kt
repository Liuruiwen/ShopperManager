package com.rw.homepage.presenter

import com.rw.basemvp.bean.BaseBean
import com.rw.homepage.HttpApi
import com.rw.homepage.bean.NormsItemBean
import com.rw.homepage.bean.NormsListReq
import com.rw.service.ServiceViewModule

/**
 * Created by Amuse
 * Date:2021/12/4.
 * Desc:
 */
class NormsListPresenter :HomePagePresenter() {
    /**
     * 添加规格
     */
    fun getNormsList(req: NormsListReq) {

        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            postBodyData(
                0,
                HttpApi.HTTP_GET_NORMS_LIST, NormsItemBean::class.java, true,
                mapOf("token" to bean.token), req
            )
        }

    }
}