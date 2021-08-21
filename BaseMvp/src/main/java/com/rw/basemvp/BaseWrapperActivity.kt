package com.rw.basemvp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rw.basemvp.widget.TitleView
import kotlinx.android.synthetic.main.base_layout.*

abstract class BaseWrapperActivity : AppCompatActivity() {

    var mActivity: AppCompatActivity? = null
    var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processBeforeLayout()
        setContentView(R.layout.base_layout)
        mActivity = this
        intiBaseView()
        initData(savedInstanceState,base_title_view)
    }


    override fun onBackPressed() {
        beforeFinish()
        super.onBackPressed()
    }

    /**
     * 处理创建布局之前操作
     */
    protected fun processBeforeLayout() {

    }

    abstract fun setLayout(): Int
    abstract fun initData(savedInstanceState: Bundle?,titleView: TitleView)

    fun intiBaseView() {
        base_title_view.setChildClickListener(R.id.iv_title_left, ({
            beforeFinish()
            finish()
        }))
        base_frame_layout.addView(layoutInflater.inflate(setLayout(), null))
    }

    /**
     * 按返回键之前操作
     */
    protected fun beforeFinish() {

    }

    /**
     * 是否显示标题栏
     */
    protected fun showTitle(isShow: Boolean) {
        if (isShow)
            base_title_view.visibility = View.VISIBLE
        else
            base_title_view.visibility = View.GONE
    }

    protected fun  showToast(msg:String){
        if (mToast == null) {
            mToast = Toast.makeText(this.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        mToast?.setText(msg);
        mToast?.show();
    }
}