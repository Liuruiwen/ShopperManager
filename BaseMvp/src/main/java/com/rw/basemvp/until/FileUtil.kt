package com.rw.basemvp.until

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.*

/**
 * Created by Amuse
 * Date:2020/4/1.
 * Desc:
 */
class FileUtil {
    var mContext:Context?=null
   constructor(context: Context){
       this.mContext=context.applicationContext
   }
    companion object{
        private var instance:FileUtil?=null
        fun getInstance(context:Context):FileUtil{
            if(instance==null){
                synchronized(UtilsManager::class) {
                    instance = FileUtil(context)
                }
            }

            return instance!!
        }
    }

    fun getFilePath(): File {
        var filePath:File
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            filePath = mContext?.getExternalFilesDir(null)!!
        }else {
            filePath = mContext?.getFilesDir()!!
        }

    return  filePath
    }



    /**
     * 将bitmap存成文件
     *
     * @param context
     * @param bitmap
     * @param imageName
     */
      fun saveBitmap(context:Context, bitmap: Bitmap, imageName:String ) :String{

      var   fos :FileOutputStream? = null;
        var os:OutputStream ?= null;
      var   inputStream:BufferedInputStream ?= null;
        var imageFile:File?=null
        try {
            ///storage/emulated/0/Android/data/com.ruiwenliu.kotlin/files/Pictures/com.ruiwenliu.kotlin/photo
            //生成路径
//            File filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),APP_FOLDER_PHOTO);
            val filePath=File("/"+ context.getPackageName()+"/photo/")
//            val filePath =  File(String.getString(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/", context.getPackageName(), "/photo/"));

//            Log.e("aaa->"," filePath:" + filePath.getPath() + " fileAbsolutePath:" + filePath.getAbsolutePath());
            if (!filePath.exists()) {
                val isTrue = filePath.mkdirs();
                Log.e("aaa->","is: " + isTrue);
            }

            //获取文件
            imageFile =  File(filePath, imageName);
            fos =  FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos?.flush();

            val values =  ContentValues();
            values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image");
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.TITLE, "Image.png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/");
            val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            val resolver = context.getContentResolver();
            val insertUri = resolver.insert(external, values);

            inputStream =  BufferedInputStream( FileInputStream(imageFile));
            if (insertUri != null) {
                os = resolver.openOutputStream(insertUri);
            }
            if (os != null) {
               val buffer =  ByteArray(1024 * 4);
                var len=0;
                while ((inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, len)
                }
                os.flush();
            }

            //通知系统相册刷新
            context.sendBroadcast( Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(imageFile))
            );
            return imageFile.getPath();
        } catch (e:Exception ) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                fos?.close();
                os?.close();
                inputStream?.close();
                imageFile?.delete();// 这里删除源文件不存在 但相册可见
            } catch (e:IOException ) {
                e.printStackTrace();
            }
        }
    }

}