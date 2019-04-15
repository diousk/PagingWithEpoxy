package com.example.mypaging.main.di

import androidx.lifecycle.ViewModel
import com.example.mypaging.api.AppApi
import com.example.mypaging.main.MainActivity
import com.example.mypaging.main.MainView
import com.example.mypaging.main.MainViewModel
import com.kingkonglive.android.utils.injection.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class MainModule {
    @Provides
    fun provideMainView(view: MainActivity): MainView = view

    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideDiscoverViewModel(view: MainView, appApi: AppApi): ViewModel {
        return MainViewModel(view, appApi)
    }
}