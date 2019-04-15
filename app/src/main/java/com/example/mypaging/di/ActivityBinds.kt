package com.example.mypaging.di

import androidx.lifecycle.ViewModelProvider
import com.example.mypaging.main.MainActivity
import com.example.mypaging.main.di.MainModule
import com.kingkonglive.android.utils.injection.DaggerAwareViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBinds {
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun bindMainActivity(): MainActivity

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerAwareViewModelFactory):
            ViewModelProvider.Factory
}