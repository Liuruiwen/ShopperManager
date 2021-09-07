package com.rw.login.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
import com.rw.login.until.RxTimerUtil
import com.rw.service.ServiceViewModule
import com.rw.service.bean.AccountBean
import com.rw.service.bean.LoginOutBean
import io.reactivex.observers.ResourceObserver
import kotlinx.android.synthetic.main.activity_verification_code.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


@Route(path = "/login/LoginActivity")
class VerificationCodeActivity : BaseActivity<LoginPresenter>() {

    private val rxTimerUtil: RxTimerUtil by lazy {
        RxTimerUtil()
    }

    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun setLayout(): Int {
        return R.layout.activity_verification_code
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("登录/注册")
        titleView.setVisible(R.id.tv_title_right, true)
        titleView.setText(R.id.tv_title_right, "密码登录")
        titleView.setChildClickListener(R.id.tv_title_right) {
            startActivity<LoginActivity>()
        }
        listener()
        reqResult()
    }

    override fun onDestroy() {
        super.onDestroy()
        rxTimerUtil.cancel()
        LoginViewModel.destroy()
    }

    private fun listener() {
        //获取验证码
        tv_verification_code.setOnClickListener {
            countTime()
        }

        //账号输入监听
        user_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && s.length < 11) {
                    tv_verification_code.isEnabled = true
                    tv_login.visibility = View.GONE
                    tv_verification_code.text = "获取验证码"
                    tv_verification_code.setTextColor(ContextCompat.getColor(this@VerificationCodeActivity,R.color.colorWrite))
                    tv_verification_code.setBackgroundResource(R.drawable.login_button_bg_blue)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        //登录/注册按钮处理
        tv_login.setOnClickListener {
            if (isInput(1)) {
                if (tv_login.text.toString() == "登录") {
                    mPresenter?.postBodyData(
                        0,
                        HttpApi.HTTP_VERIFICATION_CODE_LOGIN, LoginBean::class.java, true,
                        VerificationCodeBean(
                            user_name.text.toString().trim(),
                            verification_code.text.toString().trim()
                        )
                    )
                } else {
                    startActivity<RegisteredActivity>(
                        "account" to user_name.text.toString().trim(),
                        "title" to "注册",
                        "type" to 1
                          )
                }

            }
        }
    }

    data class GetVerificationCode(
        val account: String
    )

    data class VerificationCodeBean(
        val account: String,
        val verificationCode: String
    )


    private fun isInput(type: Int? = 0): Boolean {
        val account = user_name.text.toString().trim()
        if (account.isEmpty()) {
            toast("请输入账号")
            return false
        }
        if (type == 1) {
            val code = verification_code.text.toString().trim()
            if (code.isEmpty()) {
                toast("请输入验证码")
                return false
            }
        }

        return true

    }


    /**
     * 请求结果处理
     */
    private fun reqResult() {
        LoginViewModel.get()?.isFinish?.observe(this, Observer {
            it?.let {
                if (it == 1) {
                    finish()
                }
            }
        })

        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_VERIFICATION_CODE -> {
                    tv_login.text = "登录"
                    tv_login.visibility = View.VISIBLE

                }
                HttpApi.HTTP_VERIFICATION_CODE_LOGIN -> {
                    val bean = it as LoginBean
                    showToast("登录成功")
                    ServiceViewModule.get()?.loginService?.value = AccountBean(
                        bean.data.userName,
                        bean.data.userId,
                        bean.data.account,
                        bean.data.token
                    )
                    ServiceViewModule.get()?.loginOutService?.postValue(null)
                    ARouter.getInstance().build("/main/MainActivity").navigation()

                    finish()
                }
                else -> showToast("让我说点什么好")
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let { bean ->
                when (bean.url) {
                    HttpApi.HTTP_GET_VERIFICATION_CODE -> {
                        tv_login.text = "注册"
                        tv_login.visibility = View.VISIBLE
                    }
                    else -> {
                        bean.message?.let { message -> toast(message) }
                    }
                }

            }

        })
    }


    /**
     * 验证码处理
     */
    @SuppressLint("SetTextI18n")
    private fun countTime() {
        if (isInput()) {
            mPresenter?.postBodyData(
                0,
                HttpApi.HTTP_GET_VERIFICATION_CODE, BaseBean::class.java, true,
                GetVerificationCode(user_name.text.toString().trim())
            )
            tv_verification_code.isEnabled = false
            tv_verification_code.text = "60s"
            tv_verification_code.setTextColor(ContextCompat.getColor(this@VerificationCodeActivity,R.color.colorPrimary))
            tv_verification_code.setBackgroundResource(R.drawable.login_button_bg_gray)
            rxTimerUtil.interval(59,object :ResourceObserver<Long?>(){
                override fun onComplete() {

                }

                override fun onNext(t: Long) {

                    if (t <1.toLong()) {//当倒计时小于0,计时结束
                        tv_verification_code?.text = "重新获取"
                        tv_verification_code.isEnabled = true
                        tv_verification_code.setTextColor(ContextCompat.getColor(this@VerificationCodeActivity,R.color.colorWrite))
                        tv_verification_code.setBackgroundResource(R.drawable.login_button_bg_blue)
                        rxTimerUtil.cancel()
                        return//使用标记跳出方法
                    }
                    tv_verification_code.text = "${t}s"
                }

                override fun onError(e: Throwable) {
                    rxTimerUtil.cancel()
                }

            })
        }
    }


}