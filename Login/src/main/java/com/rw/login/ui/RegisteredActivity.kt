package com.rw.login.ui

import android.os.Bundle
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.login.R
import com.rw.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_registered.*

class RegisteredActivity : BaseActivity<LoginPresenter>() {


    override fun getPresenter(): LoginPresenter {
        return  LoginPresenter()
    }

    override fun setLayout(): Int {
        return R.layout.activity_registered
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
          titleView.setTitle("注册")
        val account=intent.getStringExtra("account")
        user_name.text="注册账号：$account"

    }
}