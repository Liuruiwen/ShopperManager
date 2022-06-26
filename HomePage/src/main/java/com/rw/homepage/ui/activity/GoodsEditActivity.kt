package com.rw.homepage.ui.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.AUTHORITY
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ruiwenliu.glide.library.GlideManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.permission.RxPermission
import com.rw.basemvp.until.ViewHolder
import com.rw.basemvp.widget.TitleView
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.GoodsEditNormsAdapter
import com.rw.homepage.adapter.SpinnerAdapter
import com.rw.homepage.adapter.TYPE_NORMS_ITEM_ATTRIBUTE
import com.rw.homepage.adapter.TYPE_NORMS_ITEM_HEADER
import com.rw.homepage.bean.*
import com.rw.homepage.model.GoodsEditModel
import com.rw.homepage.presenter.GoodsEditPresenter
import com.rw.homepage.ui.dialog.CameraDialog
import com.rw.homepage.until.FileStorage
import com.rw.homepage.until.setVisible
import kotlinx.android.synthetic.main.hp_activity_goods_edit.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import java.io.File


const val GOODS_EDIT_TYPE_ADD = 1//增加商品
const val GOODS_EDIT_TYPE_EDIT = 2//编辑商品

const val TYPE_CAMERA_CROP = 1002
const val TYPE_CAMERA_CODE = 1001
const val TYPE_PHONE_CROP = 1004
const val TYPE_PHONE_CODE = 1003
const val TYPE_CROP_CODE = 1005
class GoodsEditActivity : BaseActivity<GoodsEditPresenter>() {

    private var spinnerType=1
    private var goodsBean:GoodsListBean?=null

    private var goodsImageUrl: String? = null
    private var imageFile: File? = null
    private var cropUri: Uri? = null
    private var goodsId:Int=0
    private val categroyId: Int by lazy {
        intent.getIntExtra("id", 0)
    }

    private val fileStore: FileStorage? by lazy {
        FileStorage()
    }
    private var tvRight: TextView?=null

    private val mAdapter:GoodsEditNormsAdapter by lazy {
        GoodsEditNormsAdapter()
    }

    override fun setLayout(): Int {
        return R.layout.hp_activity_goods_edit
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        val type = intent.getIntExtra("type", GOODS_EDIT_TYPE_ADD)
        val goods=intent.getStringExtra("goodsItem")
        goodsId= intent.getIntExtra("goodsId", 0)

        if (!goods.isNullOrEmpty()){
            goodsBean= Gson().fromJson(goods, object : TypeToken<GoodsListBean>() {}.type)
        }
        titleView.setTitle(if (type == GOODS_EDIT_TYPE_ADD) "添加商品" else "商品编辑")
        tvRight = titleView.getView(R.id.tv_title_right)
        tvRight?.setVisible(true)
        tvRight?.text = "完成"
        tvRight?.textColor= ContextCompat.getColor(this,R.color.colorPrimary)
        tvRight?.setOnClickListener {
             if (isCommit()){
//                 "[{\"normsId\":1,\"list\":[1,2,3]},{\"normsId\":2,\"list\":[6,7,8,9,10,11,12]}]",
                 if (type== GOODS_EDIT_TYPE_ADD){
                     mPresenter?.reqAddGoods(AddGoodsReq(categroyId,
                         et_price.text.toString().trim(),
                         et_name.text.toString().trim(),
                         et_desc.text.toString().trim(),
                         goodsImageUrl?:"",
                         mAdapter.getNormsId()?:"",
                         spinnerType
                     ))
                 }else{
                     mPresenter?.editGoods(AddGoodsReq(categroyId,
                         et_price.text.toString().trim(),
                         et_name.text.toString().trim(),
                         et_desc.text.toString().trim(),
                         goodsImageUrl?:"",
                         mAdapter.getNormsId()?:"",
                         spinnerType,
                         goodsId
                     ))
                 }

             }
        }
        tv_add_norms.setOnClickListener {//添加编辑规格
            var jsonData=""
            if (type==GOODS_EDIT_TYPE_EDIT){
                jsonData=Gson().toJson(mAdapter.data)
            }
            startActivity<NormsListActivity>("categoryId" to categroyId.toString(),"data" to jsonData)
        }

        add_goods_image?.setOnClickListener {//上传商品图片
            val subscribe = RxPermission(this).request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).subscribe {
                if (it){

                    showCameraDialog()

                }
            }

        }

        rv_norms_list.apply {
            layoutManager = GridLayoutManager(this@GoodsEditActivity, 6)
            adapter = mAdapter
        }
        mAdapter.apply {
            setGridSpanSizeLookup { _, _, position ->  mAdapter.data[position].getSpanSize() }
        }


        GoodsEditModel.get()?.normsList?.observe(this, Observer {list->
             if (!list.isNullOrEmpty()){
                 processNormsList(list)
                 tv_add_norms.text=getString(R.string.hp_edit_norms)
             }else{
                 tv_add_norms.text=getString(R.string.hp_add_norms)
             }
        })

        processSpinner()
        reqResult()
        processEditData()
    }

    override fun getPresenter(): GoodsEditPresenter {
        return GoodsEditPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        GoodsEditModel.destroy()
    }
    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_ADD_GOODS -> {
                    showToast("添加商品成功")
                    setResult(2001)
                    finish()
                }
                HttpApi.HTTP_UPLOAD_IMAGE->{

                    if (it is UploadResultBean){
                        goodsImageUrl="${mPresenter?.getBaseUrl()}${it.data}"
                        GlideManager
                            .getInstance(this)?.loadRoundImage(goodsImageUrl,add_goods_image,15)
                    }

                    showToast("上传商品图片成功")
                }
                HttpApi.HTTP_EDIT_GOODS->{
                    if (it is GoodsBean&&!it.data.isNullOrEmpty()){
                        showToast("编辑商品成功")
                      val item=Gson().toJson(it.data?.get(0))
                        intent.putExtra("goods",item)
                        setResult(2001,intent)
                    }
                    finish()
                }
                else -> showToast("系统异常")
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let { bean ->
                bean.message?.let { message ->
                    showToast(message)
                }

            }

        })
    }


    /**
     * 完成处理
     */
    private fun isCommit():Boolean{
        val tvPrice=et_price.text.toString().trim()
        if (tvPrice.isEmpty()){
            showToast(getString(R.string.hp_input_goods_price))
            return false
        }
        val tvName=et_name.text.toString().trim()
        if (tvName.isEmpty()){
            showToast(getString(R.string.hp_input_goods_name))
            return false
        }
        val tvDesc=et_desc.text.toString().trim()
        if (tvDesc.isEmpty()){
            showToast(getString(R.string.hp_input_goods_desc))
            return false
        }


        return true
    }

    /**
     * 上下架处理
     */
    private fun processSpinner(){
        val list=arrayListOf(SpinnerBean(1,"上架"),SpinnerBean(2,"下架"))
        val spinnerAdapter= SpinnerAdapter(this, list)
        spinner.adapter=spinnerAdapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerType = list[position].type
            }

        }
    }

    /**
     * 处理规格数据
     */
    private fun processNormsList(list:List<MultiItemBean>){
        mAdapter.data.clear()
        val listNorms=ArrayList<MultiItemBean>()
        var itemHeader:NormsHeaderBean?=null
        for (index in list.indices){
            val item=list[index]
            if (item is NormsHeaderBean){
                itemHeader=item
            }else if (item is NormsAttributeBean && item.selectType==1){
                    if (itemHeader!=null){
                        listNorms.add(itemHeader)
                        itemHeader=null
                    }
                    listNorms.add(item)
                }


        }
        mAdapter.setNewInstance(listNorms)
    }

    /**
     * 处理编辑数据
     */
    private fun processEditData(){
        goodsBean?.apply {
            et_price?.setText(goodsPrice)
            et_name?.setText(goodsName)
            et_desc?.setText(goodsDesc)
            spinner?.setSelection(if (shelvesType==1) 0 else 1)
            if (!goodsImage.isNullOrEmpty()){
                goodsImageUrl=goodsImage
                GlideManager
                    .getInstance(this@GoodsEditActivity)?.loadRoundImage(goodsImage,add_goods_image,15)
            }

            processEditNorms(listNorms)
        }
    }


    /**
     * 编辑商品规格数据
     */
    private fun processEditNorms(list:List<MultiItemBean>?){
        mAdapter.data.clear()
        val listNorms=ArrayList<MultiItemBean>()
        list?.forEach {hearBean->
            if (hearBean is NormsHeaderBean){
                hearBean.itemType= TYPE_NORMS_ITEM_HEADER
                listNorms.add(hearBean)
                hearBean.listAttribute?.forEach{attributeBean->
                    attributeBean.itemType= TYPE_NORMS_ITEM_ATTRIBUTE
                    listNorms.add(attributeBean)
                }
            }

        }
        mAdapter.setNewInstance(listNorms)
    }


    private fun uploadImage(path:File){
        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), path);
        val imageBodyPart = MultipartBody.Part.createFormData("file", path.getName(), imageBody);


        mPresenter?.uploadImage(HttpApi.HTTP_UPLOAD_IMAGE,UploadResultBean::class.java, true,imageBodyPart)
    }


    private fun showCameraDialog(){
        object : CameraDialog(this){
            override fun helper(helper: ViewHolder?) {
                super.helper(helper)

                helper?.setOnClickListener(View.OnClickListener {

                    when(it?.id){
                        R.id.tv_camera->{
                            openCamera(true)
                            dismiss()
                        }
                        R.id.tv_phone->{
                            openAlbum(true)
                            dismiss()
                        }
                        R.id.tv_cancel->{
                            dismiss()
                        }
                    }
                }, R.id.tv_camera, R.id.tv_phone, R.id.tv_cancel)
            }
        }.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TYPE_CAMERA_CROP->{

                cropUri?.let {
                    cropPhoto(it)
                }

            }
            TYPE_CAMERA_CODE->{

                imageFile?.let {
                    uploadImage(it)
                }

            }
            TYPE_PHONE_CROP->{
                data?.data?.let {

                }
                if (data != null && data.getData() != null) {
                    cropPhoto(data.getData());
                }

            }
            TYPE_CROP_CODE->{
                imageFile?.let {
                    uploadImage(it)
                }
            }

        }
    }


    /**
     * 打开系统相机获取图片
     */
    fun openCamera(isCrop: Boolean) {
//        imageFile = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        imageFile= fileStore?.createCameraFile()
        imageFile?.let {
            /*调用系统拍照*/
            val intent = Intent()
            cropUri = null
            try {
                //API>=24 android 7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cropUri = FileProvider.getUriForFile(
                        this,
                        "com.rw.shoppermanager.fileprovider",
                        it
                    )
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //添加这一句表示对目标应用临时授权该Uri所代表的文件
                } else { //<24
                    cropUri = Uri.fromFile(imageFile)
                }
                intent.action = MediaStore.ACTION_IMAGE_CAPTURE //设置Action为拍照
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri)
                val cameraUrl = String.format("%1s%2s", "file://", it.getPath()) //相机存储地址
                startActivityForResult(
                    intent,
                    if (isCrop) TYPE_CAMERA_CROP  else TYPE_CAMERA_CODE
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    /**
     * 打开系统相册获取图片
     */
   private fun openAlbum(isCrop: Boolean) {

        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(
            intent,
            if (isCrop) TYPE_PHONE_CROP else TYPE_PHONE_CODE
        )
    }

    /**
     * 调用系统裁剪功能
     */
   private  fun cropPhoto(uri: Uri?) {
        imageFile = fileStore?.createCameraFile()
        imageFile?.let {
            val outputUri = Uri.fromFile(it) //缩略图保存地址
            val intent = Intent("com.android.camera.action.CROP")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            intent.setDataAndType(uri, "image/*")
            intent.putExtra("crop", "true")
            if (Build.MANUFACTURER.equals("HUAWEI")) {
                intent.putExtra("aspectX", 9998);
                intent.putExtra("aspectY", 9999);
            } else {
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
            }
            intent.putExtra("scale", true)
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.putExtra("noFaceDetection", true)
            val   cameraUrl = String.format("%1s%2s", "file://", outputUri.path) //相机存储地址
            startActivityForResult(intent, TYPE_CROP_CODE)
        }


    }




}