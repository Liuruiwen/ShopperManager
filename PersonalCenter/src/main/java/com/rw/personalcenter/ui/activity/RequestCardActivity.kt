package com.rw.personalcenter.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.personalcenter.R
import com.rw.personalcenter.adapter.RequestCardAdapter
import com.rw.personalcenter.presenter.RequestCardPresenter
import kotlinx.android.synthetic.main.pc_activity_request_card.*

class RequestCardActivity :  BaseActivity<RequestCardPresenter>()  {
  private val mAdapter: RequestCardAdapter by lazy {
      RequestCardAdapter()
  }

    override fun setLayout(): Int {
        return R.layout.pc_activity_request_card
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {

        rv_list?.apply {
            layoutManager=LinearLayoutManager(this@RequestCardActivity)
            adapter=mAdapter
        }
    }

    override fun getPresenter(): RequestCardPresenter {
       return RequestCardPresenter()
    }
}