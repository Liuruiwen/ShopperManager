package com.rw.basemvp.until

import android.view.View

/**
 * Created by Amuse
 * Date:2021/9/9.
 * Desc:
 */
fun View.setVisible(boolean: Boolean){
    visibility=if (boolean)View.VISIBLE else View.GONE
}

