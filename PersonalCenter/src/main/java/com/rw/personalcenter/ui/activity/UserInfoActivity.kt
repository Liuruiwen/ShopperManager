package com.rw.personalcenter.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.until.ViewHolder
import com.rw.basemvp.widget.TitleView
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.R
import com.rw.personalcenter.bean.UserInfoBean
import com.rw.personalcenter.presenter.PersonalCenterPresenter
import com.rw.personalcenter.ui.dialog.UserEditDialog
import com.rw.service.ServiceViewModule
import kotlinx.android.synthetic.main.pc_activity_user_info.*
import org.jetbrains.anko.toast

class UserInfoActivity : BaseActivity<PersonalCenterPresenter>() {
    private var inputDesc:String?=null
    private var inputType=0

    override fun setLayout(): Int {
        return R.layout.pc_activity_user_info
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
       titleView.setTitle("个人资料")
        initView()
        reqResult()
        click()
    }

    override fun getPresenter(): PersonalCenterPresenter {
       return PersonalCenterPresenter()
    }


    private fun initView(){
        val user=intent.getStringExtra("user")
        if (!user.isNullOrEmpty()){
            val bean= Gson().fromJson(user, UserInfoBean::class.java)
           tv_get_age.text= bean?.data?.age.toString()
            tv_get_nickname.text= bean?.data?.userName
            tv_get_address.text=bean?.data?.address
        }

    }

    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_EDIT_USER -> {
                    when(inputType){
                        2->tv_get_nickname.text=inputDesc
                       3->tv_get_age.text=inputDesc
                       4->tv_get_address.text=inputDesc
                    }
                  toast("更新成功")

                }

                else -> showToast("系统异常")
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let { bean ->
                bean.message?.let { message ->
                    showToast(message)
                }

            }

        })
    }


    private fun click(){
        tv_get_nickname.setOnClickListener {

            showLevelDialog(getString(R.string.pc_input_nickname),tv_get_nickname.text.toString(),2)
        }
        tv_get_address.setOnClickListener {
            showLevelDialog(getString(R.string.pc_input_age),tv_get_address.text.toString(),4)
        }
        tv_get_age.setOnClickListener {
            showLevelDialog(getString(R.string.pc_input_address),tv_get_age.text.toString(),3)
        }
    }

    data class EditUserReq(
           val content:String,
           val type:Int
    )

    /**
     * 编辑员工信息
     */
    private fun reqEditUser(token: String,bean:EditUserReq) {
        mPresenter?.postBodyData(
            0,
            HttpApi.HTTP_EDIT_USER, BaseBean::class.java, true,
            mapOf("token" to token), bean
        )
    }


    /**
     * 显示弹窗
     */
    private fun showLevelDialog(hint:String,desc:String?,type:Int) {
        object : UserEditDialog(this){
            override fun helper(helper: ViewHolder?) {
                super.helper(helper)
                val etDesc=helper?.getView<EditText>(R.id.et_desc)
                desc?.apply {
                    etDesc?.setText(desc)
                }
                etDesc?.hint=hint
                helper?.setOnClickListener(View.OnClickListener {

                    when(it?.id){
                        R.id.tv_cancel->{
                            dismiss()
                        }
                        R.id.tv_confirm->{
                            val text=etDesc?.text.toString().trim()
                            if (text.isEmpty()){
                                toast(hint)
                            }

                            ServiceViewModule.get()?.loginService?.value?.let { bean ->
                                inputDesc=text
                                inputType=type
                                reqEditUser(bean.token,EditUserReq(text,type))
                            }

                            dismiss()
                        }
                    }
                }, R.id.tv_cancel, R.id.tv_confirm)
            }
      }.show()
    }
}