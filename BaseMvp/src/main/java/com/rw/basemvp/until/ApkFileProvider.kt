package com.rw.basemvp.until

import androidx.core.content.FileProvider

/**
 * Created by Amuse
 * Date:2020/3/19.
 * Desc:
 */
class ApkFileProvider : FileProvider() {
    override fun onCreate(): Boolean {
        return super.onCreate()
    }
}