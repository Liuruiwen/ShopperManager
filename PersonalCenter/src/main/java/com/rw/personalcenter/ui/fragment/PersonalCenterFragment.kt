package com.rw.personalcenter.ui.fragment

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.ruiwenliu.glide.library.GlideManager
import com.rw.basemvp.BaseFragment
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.R
import com.rw.personalcenter.bean.UserInfoBean
import com.rw.personalcenter.model.UserModel
import com.rw.personalcenter.presenter.PersonalCenterPresenter
import com.rw.personalcenter.ui.activity.EmployeesManagerActivity
import com.rw.personalcenter.ui.activity.RequestCardActivity
import com.rw.personalcenter.ui.activity.SetActivity
import com.rw.personalcenter.ui.activity.UserInfoActivity
import com.rw.personalcenter.until.setVisible
import com.rw.service.ServiceViewModule
import kotlinx.android.synthetic.main.pc_fragment.*
import kotlinx.android.synthetic.main.pc_fragment.tv_address
import kotlinx.android.synthetic.main.pc_fragment.tv_nickname
import org.jetbrains.anko.startActivity

/**
 * Created by Amuse
 * Date:2021/4/15.
 * Desc:
 */
@Route(path = "/pc/PersonalCenterFragment")
class PersonalCenterFragment : BaseFragment<PersonalCenterPresenter>() {
    private var userBean:UserInfoBean?=null
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
        ServiceViewModule.get()?.loginService?.observe(this, Observer {
            if (it!=null&&userBean==null){

                reqUserData(it.token)
            }
//            else if(it==null){
//                mContext?.finish()
//            }
        })
        click()
        ServiceViewModule.get()?.loginOutService?.observe(this, Observer {
            if (it!=null){
                mContext?.finish()
            }
        })
//        ServiceViewModule.get()?.loginOutService?.observeForever {
//            if (it!=null){
//                mContext?.finish()
//            }
//
//        }

    }

//    override fun lazyData() {
//        super.lazyData()
//        if (userBean==null){
//            val loginBean=ServiceViewModule.get()?.loginService?.value
//            if (loginBean!=null){
//                reqUserData(loginBean.token)
//            }
//        }
//
//
//    }

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
                    userBean=bean
                    tv_account.text=bean.data.account
                    tv_nickname.text=bean.data.employees?.nickName+"\u3000\u3000${bean.data.userName}"
                    tv_desc.text="职责：${bean.data.employees?.content}"
                    GlideManager
                        .getInstance(context!!)?.loadCircleImage("${mPresenter?.getBaseUrl()+bean.data.headerImage}",iv_header)
                    layout_employess.setVisible(bean.data.employees?.level==1)
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

        UserModel.get()?.userInfo?.observe(viewLifecycleOwner, Observer {
           it?.let { bean->
               GlideManager
                   .getInstance(context!!)?.loadCircleImage("${mPresenter?.getBaseUrl()+bean.headerUrl}",iv_header)

           }
        })
    }

    /**
     * 获取个人中心数据
     */
    private fun reqUserData(token:String){

        mPresenter?.postBodyData(0,
            HttpApi.HTTP_GET_USER_INFO, UserInfoBean::class.java, true, linkedMapOf("token" to token))
    }

    /**
     * 点击事件处理
     */
    private fun click(){
        tv_set.setOnClickListener {
            mContext?.startActivity<SetActivity>()
        }

        tv_employees.setOnClickListener {
            mContext?.startActivity<EmployeesManagerActivity>()
        }

        tv_address.setOnClickListener { //修改店铺地址
            userBean?.let {
                ARouter.getInstance().build("/map/AddAddressActivity")
                    .withInt("id",it.data.shopAddress?.id?:0)
                    .withString("latitude",it.data.shopAddress?.latitude?:"")
                    .withString("longitude",it.data.shopAddress?.longitude?:"")
                    .navigation()
            }

        }
        layout_user?.setOnClickListener {
            var desc=""
            userBean?.let {
                desc=Gson().toJson(it)
            }

            mContext?.startActivity<UserInfoActivity>("user" to desc)
        }

        tv_request_crad_list.setOnClickListener {//员工缺勤记录点击
            mContext?.startActivity<RequestCardActivity>()
        }
    }
}