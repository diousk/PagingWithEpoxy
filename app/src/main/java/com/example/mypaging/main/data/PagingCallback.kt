package com.example.mypaging.main.data

import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.Disposable


interface PagingCallback<Response, Result> {
    /**
     * Gets called when first fetch is successful
     *
     * @param response  represents the most top model
     * @param callback represents [PageKeyedDataSource.LoadInitialCallback]
     * @param model    represents the actual response
     */
    fun onInitialSuccess(
        response: Response,
        callback: PageKeyedDataSource.LoadInitialCallback<Long, Result>,
        model: List<Result>
    )

    /**
     * Gets called when there is any error at first fetch
     *
     * @param throwable represents any error
     */
    fun onInitialError(
        params: PageKeyedDataSource.LoadInitialParams<Long>,
        callback: PageKeyedDataSource.LoadInitialCallback<Long, Result>,
        throwable: Throwable)

    /**
     * Gets called when next fetches are successful
     *
     * @param response  represents the most top model
     * @param callback represents [paging.PageKeyedDataSource.LoadInitialCallback]
     * @param model    represents the actual response
     */
    fun onPaginationSuccess(
        response: Response,
        callback: PageKeyedDataSource.LoadCallback<Long, Result>,
        params: PageKeyedDataSource.LoadParams<Long>,
        model: List<Result>
    )

    /**
     * Gets called when there is any error at next fetches
     *
     * @param throwable represents any error
     */
    fun onPaginationError(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, Result>,
        throwable: Throwable)

    /**
     * Clears references
     */
    fun clear()

    /**
     * add disposable for rxjava
     */
    fun addDisposable(disposable: Disposable)
}