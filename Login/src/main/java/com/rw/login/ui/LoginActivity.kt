package com.rw.login.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.login.HttpApi
import com.rw.login.R
import com.rw.login.bean.LoginBean
import com.rw.login.model.LoginViewModel
import com.rw.login.presenter.LoginPresenter
import com.rw.service.ServiceViewModule
import com.rw.service.bean.AccountBean
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast


class LoginActivity : BaseActivity<LoginPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        click()
    }



    data class LoginReq(
            var account: String? ,
            var password: String?
    )


    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun setLayout(): Int {
        return R.layout.activity_login
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("密码登录")
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_LOGIN_URL -> {
                    val bean = it as LoginBean
                    showToast("登录成功")
                    ServiceViewModule.get()?.loginService?.value= AccountBean(bean.data.userName,bean.data.userId,bean.data.account,bean.data.token)
                    ARouter.getInstance().build("/main/MainActivity").navigation()
                    LoginViewModel.get()?.isFinish?.value=1
                    finish()

                }
                else -> showToast("系统异常")
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let {bean->
                bean.message?.let { message -> toast(message) }
            }

        })
    }

    private fun click(){
        login.setOnClickListener {
            val user = username.text.toString().trim()
            val psd = password.text.toString().trim()
            mPresenter?.postBodyData(0,
                HttpApi.HTTP_LOGIN_URL, LoginBean::class.java, true
                ,
                LoginReq(user, psd)
            )
        }


    }

}