package com.rw.basemvp

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import com.rw.basemvp.widget.TitleView
import kotlinx.android.synthetic.main.base_layout.*


abstract class BaseWrapperActivity : AppCompatActivity() {

    var mActivity: AppCompatActivity? = null
    var mToast: Toast? = null
    var immersionBar: ImmersionBar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processBeforeLayout()
        //状态栏https://github.com/hrandroid/ImmersionBar
        immersionBar=ImmersionBar.with(this)
        immersionBar?.transparentBar()?.statusBarDarkFont(true)?.init()
        setContentView(R.layout.base_layout)

        state_height?.layoutParams?.height=getStatusBarHeight(this)


        mActivity = this
        intiBaseView()
        initData(savedInstanceState,base_title_view)
    }




    override fun onDestroy() {
        super.onDestroy()
        immersionBar?.destroy()
    }

    /**
     * 处理创建布局之前操作
     */
    protected open fun processBeforeLayout() {

    }

    abstract fun setLayout(): Int
    abstract fun initData(savedInstanceState: Bundle?,titleView: TitleView)

  open  fun intiBaseView() {
        base_title_view.setChildClickListener(R.id.iv_title_left, ({
            beforeFinish()

        }))
        base_frame_layout.addView(layoutInflater.inflate(setLayout(), null))
    }

    /**
     * 按返回键之前操作
     */
    open fun beforeFinish() {
        finish()
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

    /**
     * 获取状态栏的高度
     */
    private  fun getStatusBarHeight(activity: Activity): Int {
        val resourceId =
            activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            activity.resources.getDimensionPixelSize(resourceId)
        } else 0
    }
}