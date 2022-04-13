package com.ruiwenliu.glide.library

import android.util.Log
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * Created by Amuse
 * Date:2020/3/18.
 * Desc:
 */
class ProgressResponseBody constructor(private var url:String,private var responseBody: ResponseBody?) :ResponseBody() {


    companion object{
        private val TAG = "ProgressResponseBody"
    }


    private var bufferedSource: BufferedSource? = null


     var listener: ProgressListener? = null

   init {
        listener = ProgressInterceptor.LISTENER_MAP.get(url)
    }

    override fun contentLength(): Long {
        return responseBody!!.contentLength()
    }

    override fun contentType(): MediaType? {
        return responseBody!!.contentType()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            if(responseBody==null){
                Log.d("=======ooo","不会又是空的吧啦啦啦啦啦啦啦啦")
            }

            bufferedSource = Okio.buffer(ProgressSource(responseBody!!.source(),
                responseBody!!,listener))
        }
        return bufferedSource!!
    }

    private class  ProgressSource(delegate: Source,var responseBody: ResponseBody,var listener:ProgressListener?) : ForwardingSource(delegate) {
        var totalBytesRead: Long = 0

        var currentProgress = 0

        var dataNull=null

        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            val fullLength: Long = responseBody?.contentLength() as Long
            if (bytesRead == -1L) {
                totalBytesRead = fullLength
            } else {
                totalBytesRead += bytesRead
            }
            val progress = (100f * totalBytesRead / fullLength).toInt()
            Log.d(ProgressResponseBody.TAG, "download progress is $progress")
            if (progress != currentProgress) {
                listener?.onProgress(progress)
            }
            if (listener != null && totalBytesRead == fullLength) {
                 //缺一个移除处理
                listener=null
            }
            currentProgress = progress
            return bytesRead

        }
    }
}