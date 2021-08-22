package com.rw.login.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.login.HttpApi
import com.rw.login.R
import com.rw.login.bean.LoginBean
import com.rw.login.model.LoginViewModel
import com.rw.login.presenter.LoginPresenter
import com.rw.service.ServiceViewModule
import com.rw.service.bean.AccountBean
import kotlinx.android.synthetic.main.activity_registered.*
import kotlinx.android.synthetic.main.activity_registered.user_name
import org.jetbrains.anko.toast

class RegisteredActivity : BaseActivity<LoginPresenter>() {


    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun setLayout(): Int {
        return R.layout.activity_registered
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("注册")
        initView()
        reqResult()
    }

    private fun initView() {
        val account = intent.getStringExtra("account")
        user_name.text = "注册账号：$account"
        tv_registered.setOnClickListener {
            if (isInput()) {
                account?.let {
                    mPresenter?.postBodyData(
                        0, HttpApi.HTTP_ACCOUNT_REGISTERED,
                        LoginBean::class.java, true, RegisteredReqBean(
                            account,
                            shopper_license.text.toString().trim(),
                            password.text.toString().trim(),
                            1
                        )
                    )
                }

            }
        }
    }

    data class RegisteredReqBean(
        val account: String,
        val businessLicense: String,
        val password: String,
        val level: Int
    )

    private fun isInput(): Boolean {
        val pwd = password.text.toString().trim()
        if (pwd.isEmpty()) {
            toast("请输入账号")
            return false
        }

        val confirmPwd = confirm_password.text.toString().trim()
        if (confirmPwd.isEmpty()) {
            toast("请输入确认密码")
            return false
        }

        if (pwd != confirmPwd) {
            toast("两次密码输入的不一致")
            return false
        }

        val shopperLicense = shopper_license.text.toString().trim()
        if (confirmPwd.isEmpty()) {
            toast("请输入商家许可证")
            return false
        }

        return true

    }

    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            if (it.requestType == HttpApi.HTTP_ACCOUNT_REGISTERED) {
                val bean = it as LoginBean
                showToast("注册成功")
                ServiceViewModule.get()?.loginService?.value = AccountBean(
                    bean.data.userName,
                    bean.data.userId,
                    bean.data.account,
                    bean.data.token
                )
                finish()
                LoginViewModel.get()?.isFinish?.value=1
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let {bean->
                bean.message?.let { message -> toast(message) }
            }

        })
    }
}