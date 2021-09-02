package com.rw.personalcenter.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.until.OnViewHolder
import com.rw.basemvp.until.ViewHolder
import com.rw.basemvp.widget.TitleView
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.R
import com.rw.personalcenter.adapter.PersonalManagerAdapter
import com.rw.personalcenter.bean.EmployeesManagerBean
import com.rw.personalcenter.bean.LevelBean
import com.rw.personalcenter.bean.LevelDataBean
import com.rw.personalcenter.presenter.PersonalCenterPresenter
import com.rw.personalcenter.ui.dialog.AddEmployeesDialog
import com.rw.service.ServiceViewModule
import com.rw.service.bean.AccountBean
import kotlinx.android.synthetic.main.pc_activity_personal_manager.*
import org.jetbrains.anko.toast

class EmployeesManagerActivity : BaseActivity<PersonalCenterPresenter>() {
  private val mAdapter:PersonalManagerAdapter by lazy {
      PersonalManagerAdapter()
  }
    var levelList:List<LevelBean>?=null

    override fun getPresenter(): PersonalCenterPresenter {
      return  PersonalCenterPresenter()
    }

    override fun setLayout(): Int {
        return R.layout.pc_activity_personal_manager
    }

override fun    processBeforeLayout(){}
    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("员工管理")
        titleView.setVisible(R.id.tv_title_right, true)
        titleView.setText(R.id.tv_title_right, "添加")
        titleView.setChildClickListener(R.id.tv_title_right) {
                levelList?.let {
                    showLevelDialog(it)
                }
        }
        rv_list.apply {
            layoutManager=LinearLayoutManager(this@EmployeesManagerActivity)
            adapter=mAdapter
        }

        mAdapter.setOnItemChildClickListener { _, _, position -> childClick(position) }

        ServiceViewModule.get()?.loginService?.value?.let {

            reqEmployeesLevelList(it)
            reqEmployeesList(it)
        }
        reqResult()

    }


    /**
     * 获取员工列表
     */
    private fun reqEmployeesList(bean:AccountBean){
        mPresenter?.postBodyData(0,
            HttpApi.HTTP_EMPLOYEES_LIST, EmployeesManagerBean::class.java, true,
            mapOf("token" to bean.token),EmployeesReq(bean.account,1))
    }

    /**
     * 获取员工级别列表
     */
    private fun reqEmployeesLevelList(bean:AccountBean){
        mPresenter?.postBodyData(1,
            HttpApi.HTTP_EMPLOYEES_LEVEL_LIST, LevelDataBean::class.java, true,
            mapOf("token" to bean.token))
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
                HttpApi.HTTP_EMPLOYEES_LEVEL_LIST -> {
                    val bean = it as LevelDataBean
                    levelList=bean.data
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

    private fun showLevelDialog(list:List<LevelBean>){
        AddEmployeesDialog(this,object:OnViewHolder{
            override fun helper(helper: ViewHolder) {
             val etAccount=   helper.getView<EditText>(R.id.et_account)
                val etPassword=   helper.getView<EditText>(R.id.et_password)
                val sp= helper.getView<Spinner>(R.id.spinner)
                val adapter=ArrayAdapter<LevelBean>(this@EmployeesManagerActivity,R.layout.pc_spinner_item,list)
                sp?.prompt="请选择员工级别"
                sp?.adapter=adapter
                sp?.setSelection(0)
                sp?.setOnItemClickListener { parent, view, position, id -> toast("${list[0].nickName}") }
                helper.setOnClickListener(View.OnClickListener {

                },R.id.tv_cancel,R.id.tv_confirm)
            }

        }).show()
    }
}