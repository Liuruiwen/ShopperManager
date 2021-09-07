package com.rw.personalcenter.ui.activity

import android.os.Bundle
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.personalcenter.R
import com.rw.personalcenter.presenter.PersonalCenterPresenter

class UserInfoActivity : BaseActivity<PersonalCenterPresenter>() {


    override fun setLayout(): Int {
        return R.layout.pc_activity_user_info
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {
       titleView.setTitle("个人资料")
    }

    override fun getPresenter(): PersonalCenterPresenter {
       return PersonalCenterPresenter()
    }
}