package com.rw.shoppermanager.ui

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.rw.personalcenter.ui.activity.TYPE_CAMERA_CODE
import com.rw.personalcenter.ui.activity.TYPE_CAMERA_CROP
import com.rw.personalcenter.until.FileStorage
import com.rw.shoppermanager.R
import kotlinx.android.synthetic.main.activity_test_phone.*
import java.io.File
import java.io.FileNotFoundException

class TestPhoneActivity  : AppCompatActivity()  {
    private var cameraUrl: String? = null
    private var imageFile: File? = null
    private var cropUri: Uri? = null
    private val fileStore: FileStorage by lazy {
        FileStorage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_phone)
        val dddd="file:///storage/emulated/0/photo/camera/1649692191391.png"


        //正常加载
        Glide.with(this).load(dddd).placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .dontAnimate().into(iv_phone)

//        GlideManager
//            .getInstance(this)?.loadImage(dddd,iv_phone)

        btn.setOnClickListener {
            openCamera_2()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TYPE_CAMERA_CROP ->{

            }
            TYPE_CAMERA_CODE ->{
//                val dddd=mFilePath
//                if (resultCode == RESULT_OK) {
//                    GlideManager
//                        .getInstance(this)?.loadImage(mFilePath,iv_phone)
//
//                }

                try {
                    //拿到相机存储在指定路径的图片，而后将其转化为bitmap格式，然后显示在界面上
                    uri?.let {
                        val bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(it))
                        iv_phone.setImageBitmap(bitmap)
                    }

                } catch ( e: FileNotFoundException) {
                    e.printStackTrace();
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
                cameraUrl = String.format("%1s%2s", "file://", it.getPath()) //相机存储地址
                startActivityForResult(
                    intent,
                    if (isCrop) TYPE_CAMERA_CROP  else TYPE_CAMERA_CODE
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    var fileName:String?=""
    var mFilePath:String?=""
    var uri :Uri?=null
    // 拍照后存储并显示图片
    private fun openCamera_2() {

        val fileDir =  File(Environment.getExternalStorageDirectory(),"Pictures");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        mFilePath = fileDir.getAbsolutePath()+"/"+ fileName;

        val contentValues =  ContentValues()
        //设置文件名

        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Pictures");
        }else {

            contentValues.put(MediaStore.Images.Media.DATA, mFilePath);
        }
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        uri =
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val intent =  Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, TYPE_CAMERA_CODE)
    }


}