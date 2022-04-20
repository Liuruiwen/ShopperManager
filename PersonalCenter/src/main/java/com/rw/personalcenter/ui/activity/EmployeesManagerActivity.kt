package com.rw.personalcenter.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.bean.BaseBean
import com.rw.basemvp.until.OnViewHolder
import com.rw.basemvp.until.ViewHolder
import com.rw.basemvp.widget.TitleView
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.R
import com.rw.personalcenter.adapter.PersonalManagerAdapter
import com.rw.personalcenter.adapter.SpinnerAdapter
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
    private val mAdapter: PersonalManagerAdapter by lazy {
        PersonalManagerAdapter()
    }
    private var levelList: List<LevelBean>? = null
    private var mDialog: AddEmployeesDialog? = null
    private var level = 2
    private var deletePosition: Int? = null
    override fun getPresenter(): PersonalCenterPresenter {
        return PersonalCenterPresenter()
    }

    override fun setLayout(): Int {
        return R.layout.pc_activity_personal_manager
    }

    override fun processBeforeLayout() {}
    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
        titleView.setTitle("员工管理")
        titleView.setVisible(R.id.tv_title_right, true)
        titleView.setText(R.id.tv_title_right, "添加")
        titleView.setChildClickListener(R.id.tv_title_right) {

            if (!levelList.isNullOrEmpty()) {
                showLevelDialog(levelList!!)
            } else {
                ServiceViewModule.get()?.loginService?.value?.let {
                    reqEmployeesLevelList(it)
                }
            }

        }
        rv_list.apply {
            layoutManager = LinearLayoutManager(this@EmployeesManagerActivity)
            adapter = mAdapter
        }


        mAdapter.setOnItemChildClickListener { _, _, position -> childClick(position) }


        reqEmployeesList()
        reqResult()

    }


    /**
     * 获取员工列表
     */
    private fun reqEmployeesList() {
        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            mPresenter?.postBodyData(
                0,
                HttpApi.HTTP_EMPLOYEES_LIST, EmployeesManagerBean::class.java, true,
                linkedMapOf("token" to bean.token), EmployeesReq(bean.account, 1)
            )
        }

    }

    /**
     * 获取员工级别列表
     */
    private fun reqEmployeesLevelList(bean: AccountBean) {
        mPresenter?.postBodyData(
            1,
            HttpApi.HTTP_EMPLOYEES_LEVEL_LIST, LevelDataBean::class.java, true,
            linkedMapOf("token" to bean.token)
        )
    }


    /**
     * 添加员工
     */
    private fun reqAddEmployees(token: String, bean: AddEmployeesReq) {
        mPresenter?.postBodyData(
            2,
            HttpApi.HTTP_ADD_EMPLOYEES, BaseBean::class.java, true,
            linkedMapOf("token" to token), bean
        )
    }

    /**
     * 删除员工
     */
    private fun deleteEmployees(account: String) {
        ServiceViewModule.get()?.loginService?.value?.let { bean ->
            mPresenter?.postBodyData(
                3,
                HttpApi.HTTP_DELETE_EMPLOYEES, BaseBean::class.java, true,
                linkedMapOf("token" to bean.token), DeleteEmployeesReq(account)
            )
        }

    }


    data class EmployeesReq(
        val account: String,
        val level: Int
    )

    data class AddEmployeesReq(
        val account: String,
        val password: String,
        val level: Int
    )

    data class DeleteEmployeesReq(
        val account: String
    )


    private fun reqResult() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_EMPLOYEES_LIST -> {
                    val bean = it as EmployeesManagerBean
                    mAdapter.setNewInstance(bean.data)
                }
                HttpApi.HTTP_EMPLOYEES_LEVEL_LIST -> {
                    val bean = it as LevelDataBean
                    levelList = bean.data
                    levelList?.let { list ->
                        showLevelDialog(list)
                    }

                }
                HttpApi.HTTP_ADD_EMPLOYEES -> {
                    reqEmployeesList()
                }
                HttpApi.HTTP_DELETE_EMPLOYEES -> {
                    deletePosition?.let {position->
                        mAdapter.deleteItem(position)
                    }
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

    private fun childClick(position: Int) {
        deletePosition = position
        deleteEmployees(mAdapter.getItem(position).account)
    }

    /**
     * 显示弹窗
     */
    private fun showLevelDialog(list: List<LevelBean>) {
        if (mDialog == null) {

            mDialog = AddEmployeesDialog(this, object : OnViewHolder {

                override fun helper(helper: ViewHolder?) {
                    val etAccount = helper?.getView<EditText>(R.id.et_account)
                    val etPassword = helper?.getView<EditText>(R.id.et_password)
                    val sp = helper?.getView<Spinner>(R.id.spinner)
                    val adapter = SpinnerAdapter(this@EmployeesManagerActivity, list)
                    sp?.adapter = adapter
                    level = list[0].level
                    sp?.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            level = list[position].level
                        }

                    }

                    helper?.setOnClickListener(View.OnClickListener {
                        when (it.id) {
                            R.id.tv_cancel -> {
                                mDialog?.dismiss()
                            }
                            R.id.tv_confirm -> {
                                val account = etAccount?.text.toString().trim()
                                if (account.isEmpty()) {
                                    toast("请输入账号")
                                    return@OnClickListener
                                }
                                val psd = etPassword?.text.toString().trim()
                                if (psd.isEmpty()) {
                                    toast("请输入初始密码")
                                    return@OnClickListener
                                }

                                ServiceViewModule.get()?.loginService?.value?.let { bean ->
                                    reqAddEmployees(
                                        bean.token,
                                        AddEmployeesReq(account, psd, level)
                                    )
                                }
                                etAccount?.setText("")
                                etPassword?.setText("")
                               mDialog?.dismiss()
                            }
                        }
                    }, R.id.tv_cancel, R.id.tv_confirm)
                }

            })
        }
        mDialog?.show()
    }
}