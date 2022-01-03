package com.rw.map

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.presenter.BaseView
import com.rw.basemvp.presenter.MvpPresenter
import java.util.ArrayList

/**
 * Created by Amuse
 * Date:2022/1/3.
 * Desc:
 */
abstract class BaseMapActivity<P : MvpPresenter<BaseView>> :BaseActivity<P>(){
    //是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private var needCheckBackLocation: Boolean = false
    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private val BACK_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION"
    /**
     * 需要进行检测的权限数组
     */
    protected var needPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        BACK_LOCATION_PERMISSION
    )
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private var isNeedCheck = true
    private val PERMISSON_REQUESTCODE = 0

    override fun processBeforeLayout() {
        super.processBeforeLayout()
        if (Build.VERSION.SDK_INT > 28
            && applicationContext.applicationInfo.targetSdkVersion > 28
        ) {
            needPermissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                BACK_LOCATION_PERMISSION
            )
            needCheckBackLocation = true
        }
    }


    override fun onResume() {
        super.onResume()
        try {
            super.onResume()
            if (Build.VERSION.SDK_INT >= 23) {
                if (isNeedCheck) {
                    checkPermissions(needPermissions)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    /**
     * @param
     * @since 2.5.0
     */
    @TargetApi(23)
    private fun checkPermissions( permissions: Array<String>) {
        try {
            if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
                val needRequestPermissonList= findDeniedPermissions(permissions)
                if (null != needRequestPermissonList
                    && needRequestPermissonList.size > 0
                ) {
                    try {
                        val array = needRequestPermissonList.toTypedArray()
                        val method = javaClass.getMethod(
                            "requestPermissions", *arrayOf<Class<*>?>(
                                Array<String>::class.java,
                                Int::class.javaPrimitiveType
                            )
                        )
                        method.invoke(this, array, 0)
                    } catch (e: Throwable) {
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    @TargetApi(23)
    private fun findDeniedPermissions(permissions: Array<String>): List<String>? {
        try {
            val needRequestPermissonList: MutableList<String> =
                ArrayList()
            if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
                for (perm in permissions) {
                    if (checkMySelfPermission(perm) != PackageManager.PERMISSION_GRANTED
                        || shouldShowMyRequestPermissionRationale(perm)
                    ) {
                        if (!needCheckBackLocation && BACK_LOCATION_PERMISSION == perm
                        ) {

                            continue
                        }
                        needRequestPermissonList.add(perm)
                    }
                }
            }
            return needRequestPermissonList
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    private fun checkMySelfPermission(perm: String): Int {
        try {
            val method = javaClass.getMethod(
                "checkSelfPermission", *arrayOf<Class<*>>(
                    String::class.java
                )
            )
            return method.invoke(this, perm) as Int
        } catch (e: Throwable) {
        }
        return -1
    }

    private fun shouldShowMyRequestPermissionRationale(perm: String): Boolean {
        try {
            val method = javaClass.getMethod(
                "shouldShowRequestPermissionRationale", *arrayOf<Class<*>>(
                    String::class.java
                )
            )
            return method.invoke(this, perm) as Boolean
        } catch (e: Throwable) {
        }
        return false
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        try {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (requestCode == PERMISSON_REQUESTCODE) {
                    if (!verifyPermissions(grantResults)) {
                        showMissingPermissionDialog()
                        isNeedCheck = false
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

//    @TargetApi(23)
//    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>?, paramArrayOfInt: IntArray) {
//        try {
//            if (Build.VERSION.SDK_INT >= 23) {
//                if (requestCode == PERMISSON_REQUESTCODE) {
//                    if (!verifyPermissions(paramArrayOfInt)) {
//                        showMissingPermissionDialog()
//                        isNeedCheck = false
//                    }
//                }
//            }
//        } catch (e: Throwable) {
//            e.printStackTrace()
//        }
//    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private fun showMissingPermissionDialog() {
        try {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("提示")
            builder.setMessage("当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限")

            // 拒绝, 退出应用
            builder.setNegativeButton(
                "取消"
            ) { dialog, which ->
                try {
                    finish()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            builder.setPositiveButton(
                "设置"
            ) { dialog, which ->
                try {
                    startAppSettings()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            builder.setCancelable(false)
            builder.show()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private fun startAppSettings() {
        try {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            )
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    open fun locationMap(){

    }
}