package com.example.mypaging

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class AutoDisposeViewModel: ViewModel() {
    open val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}