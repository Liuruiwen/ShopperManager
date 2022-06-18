package com.ruiwenliu.glide.library

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import com.bumptech.glide.gifdecoder.GifDecoder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.io.File
import java.lang.reflect.Field

/**
 * Created by Amuse
 * Date:2020/3/18.
 * Desc:
 */
class GlideManager constructor( context: Context) {
    var mContext: Context?=null
    init {
        mContext=context.applicationContext
    }

    companion object{

        private var instance: GlideManager? = null
        fun getInstance(context: Context):GlideManager?{
            if (instance==null){
                synchronized(GlideManager::class) {
                    instance = GlideManager(context)
                }
            }
            return instance
        }
    }


    /**
     * 加载普通图片
     *
     * @param url
     * @param image
     */
    fun loadImage(url: String?, image: ImageView?) {
        GlideApp.with(mContext!!)
            .load(url)
            .apply(
                RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(image!!)
    }


    /**
     * 加载普通图片
     *
     * @param url
     * @param image
     */
    fun loadImage(
        url: String?,
        image: ImageView?,
        x: Int,
        y: Int
    ) {
        GlideApp.with(mContext!!)
            .load(url)
            .apply(
                RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).override(x, y)
            )
            .into(image!!)
    }

    /**
     * 加载圆角图片
     *
     * @param url
     * @param image
     * @param round
     */
    fun loadRoundImage(
        url: String?,
        image: ImageView?,
        round: Int
    ) {
        GlideApp.with(mContext!!)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(round)))
            .into(image!!)
    }

    /**
     * 加载圆角图片
     * 通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗,设置图片压缩比例
     *
     * @param url
     * @param image
     * @param round
     * @param x
     * @param y
     */
    fun loadRoundImage(
        url: String?,
        image: ImageView?,
        round: Int,
        x: Int,
        y: Int
    ) {
        GlideApp.with(mContext!!)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(round)).override( x, y))
            .into(image!!)
    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @param image
     */
    fun loadCircleImage(url: String?, image: ImageView?) {
        if (image!=null&&mContext!=null){
            GlideApp.with(mContext!!)
                .load(url)
                .apply(
                    RequestOptions.circleCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
                        .skipMemoryCache(true) //不做内存缓存
                )
                .into(image)
        }

    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @param image
     * @param number 循环播放次数
     */
    fun loadGifImage(
        url: String?,
        image: ImageView?,
        number: Int
    ) {
        GlideApp.with(mContext!!)
            .asGif()
            .load(url)
            .apply(
                RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .placeholder(R.drawable.placeholder)
            .fallback(R.drawable.placeholder)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?, model: Any,
                    target: Target<GifDrawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onResourceReady(
                    gifDrawable: GifDrawable,
                    model: Any,
                    target: Target<GifDrawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean { //设置循环播放次数为1次
                    gifDrawable.setLoopCount(number)
                    //                        getGifduration(gifDrawable);//获取Gif时长
                    return false
                }
            }).into(image!!)
    }

    /**
     * 获取Gif时长
     */
    private fun getGifDuration(gifDrawable: GifDrawable) {
        try { // 计算动画时长
            var duration = 0
            //GifDecoder decoder = gifDrawable.getDecoder();//4.0开始没有这个方法了
            /***
             * 通过反射获取时长
             */
            val state = gifDrawable.constantState
            if (state != null) { //不能混淆GifFrameLoader和GifState类
                val gifFrameLoader = getValue(state, "frameLoader")
                if (gifFrameLoader != null) {
                    val decoder = getValue(gifFrameLoader, "gifDecoder")
                    if (decoder != null && decoder is GifDecoder) {
                        for (i in 0 until gifDrawable.frameCount) {
                            duration += decoder.getDelay(i)
                        }
                    }
                }
                Log.e("Glide4.7.1", "gif播放一次动画时长:$duration")
            }
        } catch (e: Throwable) {
        }
    }


    /**
     * 通过字段名从对象或对象的父类中得到字段的值
     *
     * @param object    对象实例
     * @param fieldName 字段名
     * @return 字段对应的值
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getValue(`object`: Any?, fieldName: String?): Any? {
        if (`object` == null) {
            return null
        }
        if (TextUtils.isEmpty(fieldName)) {
            return null
        }
        var field: Field? = null
        var clazz: Class<*>? = `object`.javaClass
        while (clazz != Any::class.java) {
            try {
                field = clazz!!.getDeclaredField(fieldName!!)
                field.isAccessible = true
                return field[`object`]
            } catch (e: Exception) { //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
//如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
            clazz = clazz!!.superclass
        }
        return null
    }


  fun downloaderImage(url :String,listener:RequestListener<File>){
    GlideApp.with(mContext!!).asFile().load(url).listener(listener).submit()
  }
}