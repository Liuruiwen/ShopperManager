package com.rw.personalcenter.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.widget.TitleView
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.R
import com.rw.personalcenter.presenter.PersonalCenterPresenter
import com.rw.service.ServiceViewModule
import com.rw.service.bean.LoginOutBean
import kotlinx.android.synthetic.main.pc_activity_set.*

class SetActivity : BaseActivity<PersonalCenterPresenter>() {


    override fun getPresenter(): PersonalCenterPresenter {
       return PersonalCenterPresenter()
    }

    override fun setLayout(): Int {
       return R.layout.pc_activity_set
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("设置")
        click()
        reqResult()
    }

    private fun click(){
        login_out?.setOnClickListener {
            ServiceViewModule.get()?.loginService?.observeForever {
                it?.let {
                    loginOut(it.token)
                }
            }
        }


        tv_update_psd.setOnClickListener {
            ARouter.getInstance().build("/login/RegisteredActivity")
                .withString("account",ServiceViewModule.get()?.loginService?.value?.account)
                .withString("title","修改密码")
                .withInt("type",2)
                .navigation()
        }

    }

    /**
     * 退出登录请求
     */
    private fun loginOut(token:String){
        mPresenter?.postBodyData(0,
            HttpApi.HTTP_LOGIN_OUT, BaseBean::class.java, true, mapOf("token" to token))
    }


    private fun reqResult(){
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_LOGIN_OUT -> {
                    ServiceViewModule.get()?.loginOutService?.value= LoginOutBean()
                    ARouter.getInstance().build("/login/LoginActivity").navigation()
                    finish()
                }
                else -> showToast("系统异常")
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let {bean->
                bean.message?.let { message ->
                    showToast(message)
                }}
        })
    }

}