package com.rw.basemvp.http

import com.rw.basemvp.bean.BaseReq
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface HttpApi {
    /**
     * get请求
     * @param url
     * @return
     */
    @GET
    fun rxGet(@Url url: String): Observable<Response<ResponseBody>>

    /**
     * get请求
     * @param url
     * @param queryMap
     * @return
     */
    @GET
    fun rxGet(@Url url: String, @QueryMap queryMap: Map<String,@JvmSuppressWildcards Any>): Observable<Response<ResponseBody>>

    /**
     * get请求
     * @param url
     * @param queryMap
     * @return
     */
    @GET
    fun rxGet(@Url url: String, @QueryMap queryMap: Map<String,@JvmSuppressWildcards Any>, @HeaderMap headMap: Map<String,@JvmSuppressWildcards Any>): Observable<Response<ResponseBody>>


    /**
     * post请求
     * @param url
     * @return
     */
    @POST
    fun rxPost(@Url url: String): Observable<Response<ResponseBody>>

    /**
     * post请求
     * @param url
     * @return
     */
    @POST
    fun rxHeadPost(@Url url: String, @HeaderMap headMap: Map<String,@JvmSuppressWildcards Any>): Observable<Response<ResponseBody>>

    /**
     * post请求
     * @param url
     * @param fieldMap
     * @return
     */
    @FormUrlEncoded
    @POST
    fun rxPost(@Url url: String, @FieldMap fieldMap: Map<String,@JvmSuppressWildcards Any>):  Observable<Response<ResponseBody>>

    /**
     * post请求
     * @param url
     * @param fieldMap
     * @return
     */
    @POST
    fun rxBodyPost(@Url url: String, @Body req:  RequestBody):  Observable<Response<ResponseBody>>

    /**
     * post请求
     * @param url
     * @param fieldMap
     * @return
     */
    @POST
    fun rxBodyPost(@Url url: String,@HeaderMap headMap: Map<String,@JvmSuppressWildcards Any>, @Body req:  RequestBody):  Observable<Response<ResponseBody>>
    /**
     * post请求
     * @param url
     * @param fieldMap
     * @return
     */
    @FormUrlEncoded
    @POST
    fun rxPost(@Url url: String, @FieldMap fieldMap: Map<String,@JvmSuppressWildcards Any>, @HeaderMap headMap: Map<String,@JvmSuppressWildcards Any>): Observable<Response<ResponseBody>>

    /**
     * post请求 加json（body）请求
     * @param url
     * @param fieldMap
     * @param bean
     * @param <T>
     * @return
    </T> */
    @FormUrlEncoded
    @POST
    fun <T> rxPostBody(@Url url: String, @FieldMap fieldMap: Map<String,@JvmSuppressWildcards Any>, @Body bean: T): Observable<Response<ResponseBody>>

    /**
     * post请求 加json（body）请求
     * @param url
     * @param bean
     * @return
     */
    @POST
    fun rxPostBody(@Url url: String, @Body bean:@JvmSuppressWildcards Any): Observable<Response<ResponseBody>>

    /**
     * post请求 加json（body）请求
     * @param url
     * @param bean
     * @param map
     * @return
     */
    @POST
    fun rxPostBody(@Url url: String, @Body bean:@JvmSuppressWildcards Any, @HeaderMap map: Map<String,@JvmSuppressWildcards Any>): Observable<Response<ResponseBody>>


    /**
     * 单张图片上传
     * @param url
     * @param file
     * @return
     */
    @Multipart
    @POST
    fun rxFileUpload(@Url url: String, @Part("description") description: RequestBody, @Part file: MultipartBody.Part): Observable<Response<ResponseBody>>

    /**
     * 单张图片上传
     * @param url
     * @param file
     * @return
     */
    @Multipart
    @POST
    fun rxFileUpload(@Url url: String, @Part file: MultipartBody.Part): Observable<Response<ResponseBody>>


    /**
     * 多张图片上传
     * @param url
     * @param map
     * @return
     */
    @Multipart
    @POST
    fun rxFileUploads(@Url url: String, @Part("description") description: RequestBody, @PartMap map: Map<String, RequestBody>): Observable<Response<ResponseBody>>

    /**
     * 多张图片上传
     * @param url
     * @param map
     * @return
     */
    @Multipart
    @POST
    fun rxFileUpload(@Url url: String, @Part("description") description: RequestBody, @PartMap map: Map<String, MultipartBody.Part>): Observable<Response<ResponseBody>>

    /**
     * 文件下载
     *
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    fun rxDownloadFile(@Header("RANGE") start: String, @Url fileUrl: String): Observable<ResponseBody>
}