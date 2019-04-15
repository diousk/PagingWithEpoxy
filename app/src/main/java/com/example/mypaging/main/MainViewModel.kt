package com.example.mypaging.main

import com.example.mypaging.AutoDisposeViewModel
import com.example.mypaging.api.AppApi
import timber.log.Timber

class MainViewModel(
    private val view: MainView,
    private val appApi: AppApi
): AutoDisposeViewModel() {
    fun fetchPosts() {
        Timber.d("fetchPosts")
    }
}