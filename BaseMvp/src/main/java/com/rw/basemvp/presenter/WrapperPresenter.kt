package com.rw.basemvp.presenter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

/*   
* @Author:      Amuser
* @CreateDate:   2019-12-18 11:36
*@Description: 
*/
abstract class WrapperPresenter<V : BaseView> : Presenter<V> {
    private var viewReference: WeakReference<V>? = null//MvpView的子类的弱引用
    private var viewClassName: String? = "" //类名 Tag
    abstract fun getBaseUrl(): String
    abstract fun getToken(): String


    override fun onDestroy(owner: LifecycleOwner) {
        viewReference?.clear()
        viewReference = null
    }

    override fun attachView(mvpView: V) {
        // 初始化请求队列，传入的参数是请求并发值。
        viewClassName = mvpView.javaClass.simpleName
        viewReference = WeakReference(mvpView)
    }

    /**
     * 检查Activity或者Fragment是否已经绑定到了Presenter层
     *
     * @return 是否已经绑定
     */
    fun isViewAttached(): Boolean {
        return viewReference != null && viewReference?.get() != null
    }

    /**
     * @return 获取实现了MvpView接口的Activity或者Fragment的引用用来实现回调
     */
    fun getView(): V?{
        return viewReference?.get()
    }

    fun getPresenterContext(): Context? {
        if(getView() is Context){
            return  getView() as Context
        }else if(getView() is Fragment){
            return  (getView() as Fragment) .context
        }
        return null
    }
}