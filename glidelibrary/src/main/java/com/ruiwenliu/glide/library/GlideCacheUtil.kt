package com.ruiwenliu.glide.library

import android.content.Context
import android.os.Looper
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import java.io.File
import java.math.BigDecimal

/**
 * Created by Amuse
 * Date:2020/3/18.
 * Desc:
 */
class GlideCacheUtil constructor( context:Context) {
    var mContext:Context?=null
    init {
        mContext=context.applicationContext
    }

    companion object{

        private var instance: GlideCacheUtil? = null
          fun getInstance(context: Context):GlideCacheUtil{
              if (instance==null){
                  synchronized(GlideCacheUtil::class) {
                      instance = GlideCacheUtil(context)
                  }
              }
              return instance!!
          }
    }


    /**
     * 清除图片磁盘缓存
     */
    fun clearImageDiskCache(): Boolean {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Thread(Runnable {
                    Glide.get(mContext!!.applicationContext).clearDiskCache()
                    // BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                }).start()
            } else {
                Glide.get(mContext!!).clearDiskCache()
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 清除图片内存缓存
     */
    fun clearImageMemoryCache(): Boolean {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(mContext!!).clearMemory()
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 清除图片所有缓存
     */
    fun clearImageAllCache() {
        clearImageDiskCache()
        clearImageMemoryCache()
        val ImageExternalCatchDir =
            mContext!!.externalCacheDir.toString() + ExternalPreferredCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR
        deleteFolderFile(ImageExternalCatchDir, true)
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    fun getCacheSize(): String? {
        try {
            return getFormatSize(getFolderSize(File(mContext!!.cacheDir.toString() + "/" + GlideCache.GLIDE_DISK_NAME)).toDouble())
            //            return getFormatSize(getFolderSize(new File(mContext.getCacheDir() + "/"+ InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList) {
                size = if (aFileList.isDirectory) {
                    size + getFolderSize(aFileList)
                } else {
                    size + aFileList.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath filePath
     * @param deleteThisPath deleteThisPath
     */
    private fun deleteFolderFile(
        filePath: String,
        deleteThisPath: Boolean
    ) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                val file = File(filePath)
                if (file.isDirectory) {
                    val files = file.listFiles()
                    for (file1 in files) {
                        deleteFolderFile(file1.absolutePath, true)
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory) {
                        file.delete()
                    } else {
                        if (file.listFiles().size == 0) {
                            file.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private fun getFormatSize(size: Double): String? {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "Byte"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 =
                BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 =
                BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 =
                BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }


}