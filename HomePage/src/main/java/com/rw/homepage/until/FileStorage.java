package com.rw.homepage.until;

import android.os.Environment;

import java.io.File;

/**
 * Created by ruiwen
 * Data:2018/9/6 0006
 * Email:1054750389@qq.com
 * Desc:
 */

public class FileStorage {
    private File cameraFile;
    private File cropFile;
    public FileStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                cameraFile= new File(Environment.getExternalStorageDirectory(),"/photo/camera");
            if (!cameraFile.exists()) {
                cameraFile.mkdirs();
            }
            //裁剪图片的文件夹地址
            cropFile= new File(Environment.getExternalStorageDirectory(), "/photo/crop");
            if (!cropFile.exists()) {
                cropFile.mkdirs();
            }
        }
    }
    public File createCameraFile() {
        return new File(cameraFile, String.format("%1$s%2$s", System.currentTimeMillis(),".png"));
    }


    public File createCropFile() {
        return new File(cropFile, String.format("%1$s%2$s", System.currentTimeMillis(),".png"));
    }

    public void destory(){
        cameraFile=null;
        cropFile=null;
    }


}
