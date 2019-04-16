package com.example.mypaging.main.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mypaging.api.AppApi
import com.example.mypaging.model.Article
import timber.log.Timber

class MainDataSourceFactory(
    private val appApi: AppApi
): DataSource.Factory<Long, Article>() {
    val sourceLiveData = MutableLiveData<MainDataSource>()

    override fun create(): DataSource<Long, Article> {
        Timber.d("MainDataSourceFactory create, postValue")
        val dataSource = MainDataSource(appApi)
        sourceLiveData.postValue(dataSource)
        return dataSource
    }
}