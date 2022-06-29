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
    var listAttribute:List<NormsAttributeBean>?,
    override var itemType: Int =TYPE_NORMS_ITEM_HEADER
):MultiItemBean {
    override fun getSpanSize(): Int {
        return 6
    }
}

data class NormsResultBean  (
    var data: NormsHeaderBean?
) : BaseBean()

data class NormsAttributeResultBean  (
    var data: NormsAttributeBean?
) : BaseBean()

data class NormsAttributeBean(
    val id:Int,
    val normsAttributeName:String,
    var selectType:Int,
    val normsId:Int,
    override var itemType: Int=TYPE_NORMS_ITEM_ATTRIBUTE
):MultiItemBean {
    override fun getSpanSize(): Int {
        return 2
    }
}
