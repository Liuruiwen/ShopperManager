package com.rw.login.until

import java.util.concurrent.TimeUnit
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * Created by Amuse
 * Date:2021/8/21.
 * Desc:
 */
class RxTimerUtil {
    private var mDisposable: Disposable? = null

    /** milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    fun timer(milliseconds: Long, next: IRxNext?) {

        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Long?>{
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(t: Long) {
                    next?.doNext(t)
                }

                override fun onError(e: Throwable) {
                    cancel()
                }

            })



    }


    /**
     *每隔milliseconds毫秒后执行next操作
     * @param milliseconds
     * @param next
     */
    fun interval(milliseconds: Long, next: IRxNext?) {
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Long?>{
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(t: Long) {
                    next?.doNext(t)
                }

                override fun onError(e: Throwable) {

                }

            })



    }


    /**
     * 取消订阅
     */
    fun cancel() {
        mDisposable?.apply {
            if (!isDisposed){
                dispose()
            }

        }
    }

    interface IRxNext {
        fun doNext(number: Long)
    }
}