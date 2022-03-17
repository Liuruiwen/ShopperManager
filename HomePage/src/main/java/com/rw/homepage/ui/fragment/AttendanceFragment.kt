package com.rw.homepage.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rw.basemvp.BaseFragment
import com.rw.homepage.HttpApi
import com.rw.homepage.R
import com.rw.homepage.adapter.AttendanceAdapter
import com.rw.homepage.bean.*
import com.rw.homepage.presenter.AttendancePresenter
import com.rw.homepage.ui.dialog.CommitCardDialog
import com.rw.homepage.ui.dialog.MessageDialog
import kotlinx.android.synthetic.main.hp_fragment_attendance.*
import kotlin.collections.ArrayList

/**
 * Created by Amuse
 * Date:2022/3/10.
 * Desc:
 */
class AttendanceFragment  : BaseFragment<AttendancePresenter>(){
    private var mMessageDialog: MessageDialog?=null
    private var mCardToast: CommitCardDialog?=null
    private var selectPosition=-1
    private var mDays:String?=null
    private var mMonth=0
    private var mYear=0
    companion object {
        private var instance: AttendanceFragment? = null

                fun getInstance(year: Int,month:Int):Fragment?{
                    instance=AttendanceFragment()
                    val build=Bundle()
                    build.putInt("month",month)
                    build.putInt("year",year)
                    instance?.arguments=build
                    return instance
                }

    }

    private val mAdapter : AttendanceAdapter by lazy {
        AttendanceAdapter()
    }

    override fun lazyData() {
        super.lazyData()
        arguments?.apply {
            mMonth=getInt("month",0)
            mYear=getInt("year",0)
            mPresenter?.getAttendance(AttendancePresenter.AttendanceReq("$mYear-$mMonth"))
        }

    }

    override fun getViewLayout(): Int {
        return R.layout.hp_fragment_attendance
    }

    override fun initView() {
        mContext?.apply {
            mMessageDialog=  mPresenter?.showMessageDialog(this,
                View.OnClickListener {

                    when(it?.id){
                        R.id.tv_cancel->{
                            mMessageDialog?.dismiss()
                        }
                        R.id.tv_confirm->{
                            mDays?.let { day->
                                mPresenter?.commitCard("$mYear-$mMonth-$day")
                            }
                            mMessageDialog?.dismiss()
                        }
                    }
                },R.id.tv_cancel,R.id.tv_confirm)

            mCardToast=object :CommitCardDialog(this){

            }
        }

        rv_attendance?.apply {
            layoutManager=GridLayoutManager(context,7)
            adapter=mAdapter
        }
       mAdapter.setOnItemClickListener { _, _, position -> itemClick(position) }
    }

    override fun loadData() {
        getViewModel()?.baseBean?.observe(this, Observer {
            when (it?.requestType) {
                HttpApi.HTTP_GET_ATTENDANCE -> {
                    if (it is AttendanceResultBean) {
                        processDateResult(it.data)

                    }

                }
                HttpApi.HTTP_COMMIT_CARD->{
                    showToast("补卡提交成功，请静待审批！")
                   selectPosition=-1
                    mDays=null
                }
                else -> showToast("系统异常")
            }
        })
    }

    override fun getPresenter(): AttendancePresenter {
        return AttendancePresenter()
    }


   private  fun processDateResult(listAttendance:List<AttendanceBean>?){
       val weeks=mPresenter?.getWeekDay(mYear, mMonth)?:1//获取当月第一天是星期几
       val days=mPresenter?.getDays(mYear, mMonth)?:0+weeks//获取总天数
       var countDay=0
       val listData=ArrayList<AttendanceBean>()
       for (index in 1 until  days){
           if (index>=weeks){
               countDay++
               var attendanceBean:AttendanceBean?=null
               if (!listAttendance.isNullOrEmpty()){
                   for (i in listAttendance.indices){
                       val today= mPresenter?.getYearMonthDay(listAttendance[i].clockDate)?:"0"
                       if (countDay==today.toInt()){
                           attendanceBean=listAttendance[i]
                           attendanceBean.days=countDay.toString()
                           break
                       }
                   }
               }

               if (attendanceBean==null){
                   listData.add(AttendanceBean(0,"","","",0,"$countDay"))
               }else{
                   listData.add(attendanceBean)
               }

           }else{
               listData.add(AttendanceBean(0,"","","",0,""))
           }
       }
       mAdapter.setNewInstance(listData)
   }

    private fun itemClick(position:Int){
        mContext?.let {
            selectPosition=position
            val item= mAdapter.getItem(position)
            if (item.clockType==1){
                return
            }
            if (!item.clockTime.isNullOrEmpty() && !item.afterWorkTime.isNullOrEmpty()){
                mCardToast?.showDialog(item.clockTime,item.afterWorkTime)
            }else{
                mDays=item.days
                mMessageDialog?.showDialog("${item.days}${it.getString(R.string.hp_card_toast)}")
            }


        }

    }

}