package com.rw.personalcenter.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.rw.basemvp.BaseFragment
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.R
import com.rw.personalcenter.bean.UserInfoBean
import com.rw.personalcenter.presenter.PersonalCenterPresenter
import com.rw.personalcenter.ui.activity.SetActivity
import com.rw.service.ServiceViewModule
import kotlinx.android.synthetic.main.pc_fragment.*
import org.jetbrains.anko.startActivity

/**
 * Created by Amuse
 * Date:2021/4/15.
 * Desc:
 */
@Route(path = "/pc/PersonalCenterFragment")
class PersonalCenterFragment : BaseFragment<PersonalCenterPresenter>() {
    override fun getViewLayout(): Int {
       return R.layout.pc_fragment
    }

    override fun initView() {


//        login_state.setOnClickListener {
//            ARouter.getInstance().build("/login/LoginActivity").navigation()
//        }
//
//        login_out.setOnClickListener {
//            ServiceViewModule.get()?.loginOutService?.value= LoginOutBean()
//        }
        ServiceViewModule.get()?.loginService?.observeForever {
           it?.let {
               reqUserData(it.token)
           }
        }
        click()
        ServiceViewModule.get()?.loginOutService?.observeForever {
            mContext?.finish()
        }

    }

    override fun lazyData() {
        super.lazyData()
        val loginBean=ServiceViewModule.get()?.loginService?.value
        if (loginBean!=null){
            reqUserData(loginBean.token)
        }else{
            showToast("这是lazyData")
            ARouter.getInstance().build("/login/LoginActivity").navigation()
        }


    }

    override fun loadData() {
        reqResult()

    }


    override fun getPresenter(): PersonalCenterPresenter {
        return PersonalCenterPresenter()
    }

    @SuppressLint("SetTextI18n")
    private fun reqResult(){
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_USER_INFO -> {
                    val bean = it as UserInfoBean
                    tv_account.text=bean.data.account
                    tv_nickname.text=bean.data.employees?.nickName
                    tv_desc.text="职责：${bean.data.employees?.content}"
                    tv_employees.visibility=if (bean.data.employees?.level==1) View.VISIBLE else View.GONE
                    tv_address.visibility=if (bean.data.employees?.level==1) View.VISIBLE else View.GONE

                }
                else -> showToast("系统异常")
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let {bean->
                when(it.type){
                    HttpApi.ERROR_TOKEN_CODE ->{
                        showToast("这是token问题")
                        ARouter.getInstance().build("/login/LoginActivity").navigation()
                    }
                    else->{bean.message?.let {message->
                       showToast(message)
                    }
                    }
                }

            }

        })
    }

    /**
     * 获取个人中心数据
     */
    private fun reqUserData(token:String){
        mPresenter?.postBodyData(0,
            HttpApi.HTTP_GET_USER_INFO, UserInfoBean::class.java, true, mapOf("token" to token))
    }

    /**
     * 点击事件处理
     */
    private fun click(){
        tv_set.setOnClickListener {
            mContext?.startActivity<SetActivity>()
        }
    }
}