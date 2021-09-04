package com.rw.shoppermanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.rw.login.until.RxTimerUtil
import com.rw.shoppermanager.R
import io.reactivex.observers.ResourceObserver
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    private val rxTimerUtil: RxTimerUtil by lazy {
        RxTimerUtil()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        iv_bg.setImageResource(R.drawable.welcome_bg)
        rxTimerUtil.interval(2,object : ResourceObserver<Long?>(){
            override fun onComplete() {

            }
            override fun onNext(t: Long) {

                if (t <1.toLong()) {//当倒计时小于0,计时结束

                    ARouter.getInstance().build("/login/LoginActivity").navigation()
                    finish()
                }

            }

            override fun onError(e: Throwable) {
                rxTimerUtil.cancel()
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        rxTimerUtil.cancel()
    }
}