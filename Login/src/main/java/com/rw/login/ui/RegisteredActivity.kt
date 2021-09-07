package com.rw.login.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.widget.TitleView
import com.rw.login.HttpApi
import com.rw.login.R
import com.rw.login.bean.LoginBean
import com.rw.login.model.LoginViewModel
import com.rw.login.presenter.LoginPresenter
import com.rw.service.ServiceViewModule
import com.rw.service.bean.AccountBean
import com.rw.service.bean.LoginOutBean
import kotlinx.android.synthetic.main.activity_registered.*
import kotlinx.android.synthetic.main.activity_registered.user_name
import org.jetbrains.anko.toast

@Route(path = "/login/RegisteredActivity")
class RegisteredActivity : BaseActivity<LoginPresenter>() {

    private var type=1

    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun setLayout(): Int {
        return R.layout.activity_registered
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {

        titleView.setTitle(intent.getStringExtra("title"))
        initView()
        reqResult()
    }

    private fun initView() {
        val account = intent.getStringExtra("account")
        val type=intent.getIntExtra("type",1)//1、注册；2、更新密码
        user_name.text =if (type==1) "注册账号：$account" else "账号：$account"
        tv_registered.text=if (type==1) "注册" else "确认"
        shopper_license.hint=if (type==1) "请输入商家许可证" else "请输入原始密码"
        tv_registered.setOnClickListener {
            if (isInput()) {
                account?.let {
                    if (type==1){
                        mPresenter?.postBodyData(
                            0, HttpApi.HTTP_ACCOUNT_REGISTERED,
                            LoginBean::class.java, true, RegisteredReqBean(
                                account,
                                shopper_license.text.toString().trim(),
                                password.text.toString().trim(),
                                1
                            )
                        )
                    }else{
                        val token=ServiceViewModule.get()?.loginService?.value?.token?:""
                        mPresenter?.postBodyData(
                            1, HttpApi.HTTP_UPDATE_PASSWORD,
                            BaseBean::class.java, true, mapOf("token" to token), UpdatePsdReqBean(
                                account,
                                password.text.toString().trim(),
                                shopper_license.text.toString().trim()
                            )
                        )
                    }

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

    data class UpdatePsdReqBean(
        val account: String,
        val password: String,
        val startPwd: String
    )

    private fun isInput(): Boolean {
        val shopperLicense = shopper_license.text.toString().trim()
        if (shopperLicense.isEmpty()) {
            toast(if (type==1) "请输入商家许可证" else "请输入原始密码")
            return false
        }

        val pwd = password.text.toString().trim()
        if (pwd.isEmpty()) {
            toast("请输入密码")
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



        return true

    }

    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when(it.requestType){
                HttpApi.HTTP_ACCOUNT_REGISTERED->{
                    val bean = it as LoginBean
                    toast("注册成功")
                    ServiceViewModule.get()?.loginService?.value = AccountBean(
                        bean.data.userName,
                        bean.data.userId,
                        bean.data.account,
                        bean.data.token
                    )
                    LoginViewModel.get()?.isFinish?.value=1
                    ARouter.getInstance().build("/main/MainActivity").navigation()
                    finish()
                }
                HttpApi.HTTP_UPDATE_PASSWORD->{
                    ServiceViewModule.get()?.loginOutService?.postValue(LoginOutBean())
//                    ServiceViewModule.get()?.loginService?.postValue(null)
                    toast("修改密码成功")
                    ARouter.getInstance().build("/login/LoginActivity").navigation()
                    finish()
                }
            }

        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let {bean->
                bean.message?.let { message -> toast(message) }
            }

        })
    }
}