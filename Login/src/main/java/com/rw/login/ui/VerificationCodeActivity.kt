package com.rw.login.ui

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.widget.TitleView
import com.rw.login.HttpApi
import com.rw.login.R
import com.rw.login.bean.LoginBean
import com.rw.login.presenter.LoginPresenter
import com.rw.login.until.RxTimerUtil
import com.rw.service.ServiceViewModule
import com.rw.service.bean.AccountBean
import kotlinx.android.synthetic.main.activity_verification_code.*
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
        titleView.setVisible(R.id.tv_title_right,true)
        titleView.setText(R.id.tv_title_right,"登录")
        titleView.setChildClickListener(R.id.tv_title_right) {
            startActivity<LoginActivity>()
        }
        listener()
        reqResult()
    }

    override fun onDestroy() {
        super.onDestroy()
        rxTimerUtil.cancel()
    }

    private fun listener() {
        //获取验证码
        tv_verification_code.setOnClickListener {
            if (isInput()) {
                mPresenter?.postBodyData(
                    0,
                    HttpApi.GET_VERIFICATION_CODE, BaseBean::class.java, true,
                    GetVerificationCode(user_name.text.toString().trim())
                )
                tv_verification_code.isEnabled = false
                tv_verification_code.setBackgroundResource(R.drawable.login_button_bg_gray)
                rxTimerUtil.timer(60000, object : RxTimerUtil.IRxNext {
                    override fun doNext(number: Long) {
                        tv_verification_code.text = "${number}秒"
                        if (number.equals(0)) {
                            tv_verification_code?.text = "重新获取"
                            tv_verification_code.isEnabled = true
                            tv_verification_code.setBackgroundResource(R.drawable.login_button_bg_blue)
                        }
                    }

                })
            }
        }

        user_name.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                   if (!s.isNullOrEmpty()&&s.length<11){
                       tv_verification_code.isEnabled = true
                       tv_login.visibility= View.GONE
                       tv_verification_code.text="获取验证码"
                       tv_verification_code.setBackgroundResource(R.drawable.login_button_bg_blue)
                   }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        tv_login.setOnClickListener {
            if (isInput(1)){
                if (tv_login.text.toString()=="登录"){
                    mPresenter?.postBodyData(
                        0,
                        HttpApi.VERIFICATION_CODE_LOGIN, LoginBean::class.java, true,
                        VerificationCodeBean(user_name.text.toString().trim(),verification_code.text.toString().trim())
                    )
                }else{
                    startActivity<RegisteredActivity>("account" to  user_name.text.toString().trim())
                }

            }
        }
    }

    data class GetVerificationCode(
        val account: String
    )

    data class VerificationCodeBean(
        val account: String,
        val verificationCode:String
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
    private fun reqResult(){
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.GET_VERIFICATION_CODE -> {
                    tv_login.text="登录"
                    tv_login.visibility= View.VISIBLE

                }
                HttpApi.VERIFICATION_CODE_LOGIN -> {
                    val bean = it as LoginBean
                    showToast("登录成功")
                    ServiceViewModule.get()?.loginService?.value= AccountBean(bean.data.userName,bean.data.userId,bean.data.account,bean.data.token)
                    finish()
                }
                else -> showToast("让我说点什么好")
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let {bean->
                when(bean.url){
                    HttpApi.GET_VERIFICATION_CODE->{
                        tv_login.text="注册"
                        tv_login.visibility= View.VISIBLE
                    }
                    else->{
                        bean.message?.let { message -> toast(message) }
                    }
                }

            }

        })
    }

}