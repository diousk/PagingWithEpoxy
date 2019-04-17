package com.example.mypaging

import android.os.Handler
import android.os.HandlerThread
import com.airbnb.epoxy.EpoxyController
import com.example.mypaging.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class App: DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        val handlerThread = HandlerThread("epoxy").apply { start() }
        Handler(handlerThread.looper).also {
            EpoxyController.defaultDiffingHandler = it
            EpoxyController.defaultModelBuildingHandler = it
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}