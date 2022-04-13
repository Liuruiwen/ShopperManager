package com.ruiwenliu.glide.library

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * Created by Amuse
 * Date:2020/3/18.
 * Desc:
 */
@GlideModule
class GlideCache  :AppGlideModule(){
    companion object{
        val GLIDE_DISK_NAME = "GlideManagerCache"
    }
    val GLIDE_DISK_SIZE = 1024 * 1024 * 200
    val GLIDE_MEMORY_SIZE = 5 * 1024 * 1024


    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        //自定义缓存目录，磁盘缓存给150M 另外一种设置缓存方式
        builder.setDiskCache(
            InternalCacheDiskCacheFactory(
                context,
                GLIDE_DISK_NAME,
                GLIDE_DISK_SIZE.toLong()
            )
        )
        builder.setMemoryCache(LruResourceCache(GLIDE_MEMORY_SIZE.toLong()  ))
        //配置图片缓存格式 默认格式为8888
        builder.setDefaultRequestOptions(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(ProgressInterceptor())
        val okHttpClient = builder.build()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )
    }
}