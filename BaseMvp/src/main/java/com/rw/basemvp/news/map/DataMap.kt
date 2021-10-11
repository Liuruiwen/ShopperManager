package com.rw.basemvp.news.map

import com.rw.basemvp.bean.MessageBean
import io.reactivex.functions.Function

/**
 * Created by Amuse
 * Date:2021/10/10.
 * Desc:
 */
class DataMap<T>:Function<MessageBean<T>,T> {

    override fun apply(t: MessageBean<T>): T {
        return t.data
    }
}