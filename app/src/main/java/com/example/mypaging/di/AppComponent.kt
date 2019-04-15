package com.example.mypaging.di

import com.example.mypaging.App
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class
])
interface AppComponent: AndroidInjector<App> {

    @Component.Factory
    interface Builder: AndroidInjector.Factory<App>
}