package com.rw.shoppermanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.rw.service.ServiceViewModule
import com.rw.shoppermanager.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_login.setOnClickListener {
            ARouter.getInstance().build("/login/LoginActivity").navigation()
        }

        ServiceViewModule.get()?.loginService?.observe(this, Observer {
            tv_login?.text=it?.account
        })
    }
}