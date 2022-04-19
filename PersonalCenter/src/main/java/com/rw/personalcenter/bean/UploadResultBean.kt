package com.rw.personalcenter.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2022/4/19.
 * Desc:
 */
data class UploadResultBean (
    var data: String
) : BaseBean()


data class EditUserBean(
    var userName:String?,
    var headerUrl:String?
)