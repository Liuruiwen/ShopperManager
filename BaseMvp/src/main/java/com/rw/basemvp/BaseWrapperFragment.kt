package com.rw.basemvp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rw.basemvp.bean.BaseModel
import com.rw.basemvp.dialog.LoadingDialog
import com.rw.basemvp.presenter.BaseView
import com.rw.basemvp.presenter.MvpPresenter


/**
 * Created by Amuse
 * Date:2020/2/11.
 * Desc:
 */
abstract class BaseWrapperFragment<P : MvpPresenter<BaseView>> : Fragment(), BaseView {

    protected var mPresenter: P? = null
    private var mLoadingDialog: LoadingDialog? = null
    protected var contentView: View? = null

    /**
     * 是否初始化过布局
     */
    private var isViewInitiated: Boolean = false


    /**
     * 是否加载过数据
     */
    private var isDataInitiated: Boolean = false
    protected var mContext: Activity? = null
    private var mToast: Toast? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Class<? extends WrapperPresenter> presenterClass = (Class<? extends WrapperPresenter>) type.getActualTypeArguments()[0];

        mPresenter = getPresenter()
        mPresenter?.attachView(this)
        mPresenter?.apply {
            lifecycle.addObserver(this)
        }


//        try {
//            val type = this.javaClass.getGenericSuperclass() as ParameterizedType
//            val presenterClass = type.actualTypeArguments[0] as Class<out WrapperPresenter<BaseView>>
//            this.mPresenter = presenterClass.newInstance() as P
//            mPresenter?.attachView(this)
//            if (mPresenter!=null){
//                lifecycle.addObserver(mPresenter!!)
//            }
//
//        } catch (e: IllegalAccessException) {
//            e.printStackTrace()
//        } catch (e: java.lang.InstantiationException) {
//            e.printStackTrace()
//        }


        mLoadingDialog = createLoadingDialog()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentView = inflater.inflate(getViewLayout(), container, false)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        loadData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewInitiated = true
        //加载数据
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
        onHideLoading()
        mLoadingDialog = null
        mPresenter?.apply {
            lifecycle.removeObserver(this)
        }

//        mPresenter = null
    }


    override fun onShowLoading() {
        Log.d("是否进来了弹窗", "========${mLoadingDialog?.isShowing}")
        if (mLoadingDialog != null && !mLoadingDialog?.isShowing!!) {
            mLoadingDialog?.show()
        }

    }

    override fun onHideLoading() {

        if (mLoadingDialog?.isShowing() ?: false) {
            mLoadingDialog?.dismiss()
        }


    }


    override fun onBeforeSuccess() {
    }

    protected fun createLoadingDialog(): LoadingDialog {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog(mContext)
            mLoadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        return mLoadingDialog as LoadingDialog
    }


    /**
     * @return 布局resourceId
     */
    abstract fun getViewLayout(): Int

    /**
     * 初始化View。或者其他view级第三方控件的初始化,及相关点击事件的绑定
     *
     */
    protected abstract fun initView()

    /**
     * 从intent中获取请求参数，初始化vo对象，并发送请求
     *
     */
    protected abstract fun loadData()

    abstract fun getPresenter(): P


    var isLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            lazyData()
            isLoaded = true
        }
    }




    /**
     * 数据懒加载
     *
     * @param
     */
    protected abstract fun lazyData()

    protected fun showToast(msg: String) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext?.application, "", Toast.LENGTH_SHORT)
        }
        mToast?.setText(msg)
        mToast?.show()
    }

    private val viewModule: BaseModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(mContext?.application!!)
            .create(BaseModel::class.java)
    }

    override fun getViewModel(): BaseModel? {
        return viewModule
    }
}