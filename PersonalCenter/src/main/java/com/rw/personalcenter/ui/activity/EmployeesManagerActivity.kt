package com.rw.personalcenter.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.R
import com.rw.personalcenter.adapter.PersonalManagerAdapter
import com.rw.personalcenter.bean.EmployeesManagerBean
import com.rw.personalcenter.presenter.PersonalCenterPresenter
import com.rw.service.ServiceViewModule
import com.rw.service.bean.AccountBean
import kotlinx.android.synthetic.main.pc_activity_personal_manager.*

class EmployeesManagerActivity : BaseActivity<PersonalCenterPresenter>() {
  private val mAdapter:PersonalManagerAdapter by lazy {
      PersonalManagerAdapter()
  }

    override fun getPresenter(): PersonalCenterPresenter {
      return  PersonalCenterPresenter()
    }

    override fun setLayout(): Int {
        return R.layout.pc_activity_personal_manager
    }

override fun    processBeforeLayout(){}
    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("员工管理")
        rv_list.apply {
            layoutManager=LinearLayoutManager(this@EmployeesManagerActivity)
            adapter=mAdapter
        }

        mAdapter.setOnItemChildClickListener { _, _, position -> childClick(position) }

        ServiceViewModule.get()?.loginService?.value?.let {
            reqEmployeesList(it)
        }
        reqResult()

    }


    private fun reqEmployeesList(bean:AccountBean){
        mPresenter?.postBodyData(0,
            HttpApi.HTTP_EMPLOYEES_LIST, EmployeesManagerBean::class.java, true,
            mapOf("token" to bean.token),EmployeesReq(bean.account,1))
    }

    data class EmployeesReq(
        val account:String,
        val level:Int
    )


    private fun reqResult(){
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_EMPLOYEES_LIST -> {
                    val bean = it as EmployeesManagerBean
                    mAdapter.setNewInstance(bean.data)
                }
                else -> showToast("系统异常")
            }
        })

        getViewModel()?.errorBean?.observe(this, Observer {
            it?.let {bean->
                bean.message?.let {message->
                    showToast(message)
                }

            }

        })
    }
    private fun childClick(position:Int){

    }
}