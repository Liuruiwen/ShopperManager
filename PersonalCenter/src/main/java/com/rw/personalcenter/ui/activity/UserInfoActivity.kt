package com.rw.personalcenter.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ruiwenliu.glide.library.GlideManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.permission.RxPermission
import com.rw.basemvp.until.ViewHolder
import com.rw.basemvp.widget.TitleView
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.R
import com.rw.personalcenter.bean.EditUserBean
import com.rw.personalcenter.bean.UploadResultBean
import com.rw.personalcenter.bean.UserInfoBean
import com.rw.personalcenter.model.UserModel
import com.rw.personalcenter.presenter.PersonalCenterPresenter
import com.rw.personalcenter.ui.dialog.CameraDialog
import com.rw.personalcenter.ui.dialog.UserEditDialog
import com.rw.personalcenter.until.FileStorage
import com.rw.service.ServiceViewModule
import kotlinx.android.synthetic.main.pc_activity_user_info.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import java.io.File

const val TYPE_CAMERA_CROP = 2
const val TYPE_CAMERA_CODE = 1
const val TYPE_PHONE_CROP = 4
const val TYPE_PHONE_CODE = 3
const val TYPE_CROP_CODE = 5
class UserInfoActivity : BaseActivity<PersonalCenterPresenter>() {
    private var inputDesc:String?=null
    private var inputType=0
    private var headerUrl: String? = null
    private var imageFile: File? = null
    private var cropUri: Uri? = null
    private val fileStore: FileStorage by lazy {
        FileStorage()
    }

    override fun setLayout(): Int {
        return R.layout.pc_activity_user_info
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
       titleView.setTitle("????????????")
        initView()
        reqResult()
        click()
    }

    override fun getPresenter(): PersonalCenterPresenter {
       return PersonalCenterPresenter()
    }


    private fun initView(){
        val user=intent.getStringExtra("user")
        if (!user.isNullOrEmpty()){
            val bean= Gson().fromJson(user, UserInfoBean::class.java)
           tv_get_age.text= bean?.data?.age.toString()
            tv_get_nickname.text= bean?.data?.userName
            tv_get_address.text=bean?.data?.address
            GlideManager
                .getInstance(this@UserInfoActivity)?.loadCircleImage("${mPresenter?.getBaseUrl()+bean?.data?.headerImage}",iv_user_header)
        }

    }

    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_EDIT_USER -> {
                    when(inputType){
                        1->{
                            UserModel.get()?.userInfo?.value= EditUserBean(tv_get_nickname.text.toString(),headerUrl)
                            GlideManager
                                .getInstance(this@UserInfoActivity)?.loadCircleImage("${mPresenter?.getBaseUrl()+headerUrl}",iv_user_header)
                        }
                        2->tv_get_nickname.text=inputDesc
                       3->tv_get_age.text=inputDesc
                       4->tv_get_address.text=inputDesc
                    }
                  toast("????????????")

                }
                HttpApi.HTTP_UPLOAD_IMAGE->{


                    ServiceViewModule.get()?.loginService?.value?.let { bean ->
                       if (it is UploadResultBean){
                           inputType=1
                           headerUrl=it.data
                           reqEditUser(bean.token,EditUserReq(it.data,inputType))
                       }

                    }
                    showToast("??????????????????")
                }
                else -> showToast("????????????")
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


    private fun click(){
        tv_get_nickname.setOnClickListener {

            showLevelDialog(getString(R.string.pc_input_nickname),tv_get_nickname.text.toString(),2)
        }
        tv_get_address.setOnClickListener {
            showLevelDialog(getString(R.string.pc_input_age),tv_get_address.text.toString(),4)
        }
        tv_get_age.setOnClickListener {
            showLevelDialog(getString(R.string.pc_input_address),tv_get_age.text.toString(),3)
        }
        layout_header.setOnClickListener {
            RxPermission(this@UserInfoActivity).request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA).subscribe {
                if (it){

                    showCameraDialog()

                }
            }

        }
    }

    data class EditUserReq(
           val content:String,
           val type:Int
    )

    /**
     * ??????????????????
     */
    private fun reqEditUser(token: String,bean:EditUserReq) {
        mPresenter?.postBodyData(
            0,
            HttpApi.HTTP_EDIT_USER, BaseBean::class.java, true,
            linkedMapOf("token" to token), bean
        )
    }

    private fun uploadImage(path:File){
        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), path);
        val imageBodyPart = MultipartBody.Part.createFormData("file", path.getName(), imageBody);


        mPresenter?.uploadImage(HttpApi.HTTP_UPLOAD_IMAGE,UploadResultBean::class.java, true,imageBodyPart)
    }


    /**
     * ????????????
     */
    private fun showLevelDialog(hint:String,desc:String?,type:Int) {
        object : UserEditDialog(this){
            override fun helper(helper: ViewHolder?) {
                super.helper(helper)
                val etDesc=helper?.getView<EditText>(R.id.et_desc)
                desc?.apply {
                    etDesc?.setText(desc)
                }
                etDesc?.hint=hint
                helper?.setOnClickListener(View.OnClickListener {

                    when(it?.id){
                        R.id.tv_cancel->{
                            dismiss()
                        }
                        R.id.tv_confirm->{
                            val text=etDesc?.text.toString().trim()
                            if (text.isEmpty()){
                                toast(hint)
                            }

                            ServiceViewModule.get()?.loginService?.value?.let { bean ->
                                inputDesc=text
                                inputType=type
                                reqEditUser(bean.token,EditUserReq(text,type))
                            }

                            dismiss()
                        }
                    }
                }, R.id.tv_cancel, R.id.tv_confirm)
            }
      }.show()
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
                imageFile?.let {

                    uploadImage(it)
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
     * ??????????????????????????????
     */
    fun openCamera(isCrop: Boolean) {
        imageFile = fileStore.createCameraFile()
       imageFile?.let {
           /*??????????????????*/
           val intent = Intent()
           cropUri = null
           try {
               //API>=24 android 7.0
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                   cropUri = FileProvider.getUriForFile(
                       this@UserInfoActivity,
                       "com.rw.shoppermanager.fileprovider",
                       it
                   )
                   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //???????????????????????????????????????????????????Uri??????????????????
               } else { //<24
                   cropUri = Uri.fromFile(imageFile)
               }
               intent.action = MediaStore.ACTION_IMAGE_CAPTURE //??????Action?????????
               intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri)
               val cameraUrl = String.format("%1s%2s", "file://", it.getPath()) //??????????????????
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
     * ??????????????????????????????
     */
    fun openAlbum(isCrop: Boolean) {

        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(
            intent,
            if (isCrop) TYPE_PHONE_CROP else TYPE_PHONE_CODE
        )
    }

    /**
     * ????????????????????????
     */
    fun cropPhoto(uri: Uri?) {
        imageFile = fileStore.createCameraFile()
        imageFile?.let {
            val outputUri = Uri.fromFile(it) //?????????????????????
            val intent = Intent("com.android.camera.action.CROP")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            intent.setDataAndType(uri, "image/*")
            intent.putExtra("crop", "true")
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("scale", true)
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.putExtra("noFaceDetection", true)
           val   cameraUrl = String.format("%1s%2s", "file://", outputUri.path) //??????????????????
           startActivityForResult(intent, TYPE_CROP_CODE)
        }


    }


}


