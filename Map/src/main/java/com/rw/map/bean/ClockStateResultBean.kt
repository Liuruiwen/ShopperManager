package com.rw.map.bean

import com.rw.basemvp.bean.BaseBean

/**
 * Created by Amuse
 * Date:2022/1/5.
 * Desc:
 */
data class ClockStateResultBean (
    val data:ClockStateBean?
):BaseBean()

data class ClockStateBean(
    val clockTime:String?,//上班打卡时间
    val afterWorkTime:String?//下班打卡时间
)