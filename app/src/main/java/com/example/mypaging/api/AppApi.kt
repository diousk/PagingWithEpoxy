package com.example.mypaging.api

import com.example.mypaging.model.Feed
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApi {
    @GET("/v2/everything")
    fun fetchFeed(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Long,
        @Query("pageSize") pageSize: Int
    ): Single<Feed>
}