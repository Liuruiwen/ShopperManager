package com.rw.personalcenter.ui.activity

import android.Manifest
import android.content.Intent
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
       titleView.setTitle("个人资料")
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
                  toast("更新成功")

                }
                HttpApi.HTTP_UPLOAD_IMAGE->{


                    ServiceViewModule.get()?.loginService?.value?.let { bean ->
                       if (it is UploadResultBean){
                           inputType=1
                           headerUrl=it.data
                           reqEditUser(bean.token,EditUserReq(it.data,inputType))
                       }

                    }
                    showToast("上传头像成功")
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
                    openCamera(false)
                }
            }

        }
    }

    data class EditUserReq(
           val content:String,
           val type:Int
    )

    /**
     * 编辑员工信息
     */
    private fun reqEditUser(token: String,bean:EditUserReq) {
        mPresenter?.postBodyData(
            0,
            HttpApi.HTTP_EDIT_USER, BaseBean::class.java, true,
            mapOf("token" to token), bean
        )
    }

    private fun uploadImage(path:File){
        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), path);
        val imageBodyPart = MultipartBody.Part.createFormData("file", path.getName(), imageBody);


        mPresenter?.uploadImage(HttpApi.HTTP_UPLOAD_IMAGE,UploadResultBean::class.java, true,imageBodyPart)
    }


    /**
     * 显示弹窗
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
        }
    }


    /**
     * 打开系统相机获取图片
     */
    fun openCamera(isCrop: Boolean) {
        imageFile = fileStore.createCameraFile()
       imageFile?.let {
           /*调用系统拍照*/
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
}


