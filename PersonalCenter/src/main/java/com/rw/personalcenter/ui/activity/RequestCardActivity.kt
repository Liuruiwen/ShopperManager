package com.rw.personalcenter.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rw.basemvp.BaseActivity
import com.rw.basemvp.widget.TitleView
import com.rw.personalcenter.HttpApi
import com.rw.personalcenter.R
import com.rw.personalcenter.adapter.RequestCardAdapter
import com.rw.personalcenter.bean.AgreeRequestCardReq
import com.rw.personalcenter.bean.DeleteRequestCardReq
import com.rw.personalcenter.bean.RequestCardBean
import com.rw.personalcenter.presenter.RequestCardPresenter
import kotlinx.android.synthetic.main.pc_activity_request_card.*

class RequestCardActivity :  BaseActivity<RequestCardPresenter>()  {
    private var mPosition=-1

  private val mAdapter: RequestCardAdapter by lazy {
      RequestCardAdapter()
  }

    override fun setLayout(): Int {
        return R.layout.pc_activity_request_card
    }

    override fun initData(savedInstanceState: Bundle?, titleView: TitleView) {

        titleView.setTitle("缺勤记录")
        rv_list?.apply {
            layoutManager=LinearLayoutManager(this@RequestCardActivity)
            adapter=mAdapter
        }
        mAdapter.addChildClickViewIds(R.id.tv_delete)
        mAdapter.addChildClickViewIds(R.id.tv_approval)
        mAdapter.setOnItemChildClickListener { _, view, position ->   itemChildClick(position,view) }
        mPresenter?.getRequestCard()
        getRequestResult()
    }

    override fun getPresenter(): RequestCardPresenter {
       return RequestCardPresenter()
    }

    /**
     * 获取请求结果
     */
    private fun getRequestResult(){
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_REQUEST_CARD -> {
                    if (it is RequestCardBean) {
                        mAdapter.setNewInstance(it.data)
                    }

                }
                HttpApi.HTTP_DELETE_REQUEST_CARD->{
                   mAdapter.removeAt(mPosition)
                    mPosition=-1
                }
                HttpApi.HTTP_AGREE_REQUEST_CARD->{
                   mAdapter.updateState(mPosition)
                    mPosition=-1
                }
                else -> showToast("系统异常")
            }
        })
    }

    private fun itemChildClick(position:Int,view: View?){
        mPosition=position
        val item=mAdapter.getItem(position)
        when(view?.id){
            R.id.tv_approval->{
                if (item.cardState==1){
                    return
                }
                mPresenter?.agreeRequestCard(AgreeRequestCardReq(item.account,item.id,item.absenteeismTime))
            }
            R.id.tv_delete->{
                mPresenter?.deleteRequestCard(DeleteRequestCardReq(item.id))
            }
        }

    }
}