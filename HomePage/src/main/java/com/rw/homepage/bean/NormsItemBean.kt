package com.rw.homepage.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rw.basemvp.bean.BaseBean
import com.rw.homepage.adapter.TYPE_NORMS_ITEM_ATTRIBUTE
import com.rw.homepage.adapter.TYPE_NORMS_ITEM_HEADER

/**
 * Created by Amuse
 * Date:2021/12/4.
 * Desc:
 */
open interface MultiItemBean : MultiItemEntity {
    fun getSpanSize(): Int
}

data class NormsItemBean  (
    var data: MutableList<NormsHeaderBean>?
) : BaseBean()

data class NormsHeaderBean(
    val id:Int,
    val normsName:String,
    val businessLicense:String,
    val categoryId:Int,
    val listAttribute:List<NormsAttributeBean>?,
    override var itemType: Int =TYPE_NORMS_ITEM_HEADER
):MultiItemBean {
    override fun getSpanSize(): Int {
        return 6
    }
}


data class NormsAttributeBean(
    val id:Int,
    val normsAttributeName:String,
    val normsId:Int,
    override var itemType: Int=TYPE_NORMS_ITEM_ATTRIBUTE
):MultiItemBean {
    override fun getSpanSize(): Int {
        return 2
    }
}
