package com.rw.shoppermanager

import android.app.Application
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.rw.base.BaseApp

/**
 * Created by Amuse
 * Date:2021/4/15.
 * Desc:
 */
class MainApplication:BaseApp() {
    override fun onCreate() {
        super.onCreate()
        // 初始化 ARouter
        // 初始化 ARouter
        if (BuildConfig.DEBUG) { // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)

        initModuleApp(this)
        initModuleData(this)
    }
    override fun initModuleApp(application: Application?) {
        for (moduleApp in AppConfig.moduleApps) {
            try {
                val clazz = Class.forName(moduleApp)
                val baseApp = clazz.newInstance() as BaseApp
                baseApp.initModuleApp(this)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }
        }
    }

    override fun initModuleData(application: Application?) {
        for (moduleApp in AppConfig.moduleApps) {
            try {
                val clazz = Class.forName(moduleApp)
                val baseApp = clazz.newInstance() as BaseApp
                baseApp.initModuleData(this)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }
        }
    }
}