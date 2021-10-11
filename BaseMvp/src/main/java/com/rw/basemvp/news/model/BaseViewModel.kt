package com.rw.basemvp.news.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by Amuse
 * Date:2021/10/11.
 * Desc:
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable:CompositeDisposable=CompositeDisposable()


    fun<T> DisposableObserver<T>.addBind(){
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}