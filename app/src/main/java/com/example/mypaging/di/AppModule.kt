package com.example.mypaging.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.mypaging.App
import com.example.mypaging.api.AppApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module(includes = [ActivityBinds::class])
class AppModule {
    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    fun providePrefs(context: Context) : SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun provideApi(context: Context) : AppApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder().baseUrl("https://newsapi.org")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
        return retrofit.create(AppApi::class.java)
    }
}