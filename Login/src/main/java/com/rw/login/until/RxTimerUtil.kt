package com.rw.login.until

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * Created by Amuse
 * Date:2021/8/21.
 * Desc:
 */
class RxTimerUtil {

    private var mDisposable: Disposable? = null
    fun interval(totalTime:Long,observer:Observer<Long?>){
         Observable.interval(totalTime,TimeUnit.SECONDS)
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(observer)
    }


    fun interval(totalTime:Long,resourceObserver: ResourceObserver<Long?>){
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take(totalTime + 1)
            .map { takeValue -> totalTime - takeValue }
            .doOnSubscribe { disposable ->
                mDisposable=disposable
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resourceObserver)
    }


    fun cancel(){
        mDisposable?.let {
           if (it.isDisposed){
               it.dispose()
           }
       }
    }




}


