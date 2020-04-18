package com.sillylife.dd.services

import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

class AppDisposable {
    private var compositeDisposable: CompositeDisposable? = null

    fun init() {
        compositeDisposable = CompositeDisposable()
    }

    fun getDisposable(): CompositeDisposable {
        if (compositeDisposable == null)
            init()
        return compositeDisposable!!
    }

    fun add(disposable: Disposable) {
        if (compositeDisposable == null)
            init()
        compositeDisposable?.add(disposable);
    }

    fun addTo(disposable: Disposable) {
        if (compositeDisposable == null) {
            init()
        }
        disposable.addTo(compositeDisposable!!)
    }

    fun dispose() {
        if (compositeDisposable == null)
            init()
        Log.d("compositeDisposable", "${compositeDisposable?.size()} --")
        compositeDisposable?.clear()
        Log.d("compositeDisposable", "${compositeDisposable?.size()} ==")
        compositeDisposable = null
    }

}