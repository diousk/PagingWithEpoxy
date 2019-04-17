package com.example.mypaging.main

import android.os.Handler
import android.os.Looper
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.example.mypaging.main.data.NetworkState
import com.example.mypaging.model.Article
import timber.log.Timber

class ArticleController(
    private val retryCallback: () -> Unit
): PagedListEpoxyController<Article>(
    modelBuildingHandler = Handler(Looper.getMainLooper()),
    diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
) {

    var networkState: NetworkState? = null
        set(value) {
            if (value != field) {
                field = value
                Timber.d("networkState = $networkState, requestDelayedModelBuild")
                requestDelayedModelBuild(0)
            }
        }

    override fun buildItemModel(currentPosition: Int, item: Article?): EpoxyModel<*> {
        Timber.d("buildItemModel, thread = ${Thread.currentThread().name}")
        return when(item) {
            // null for placeholder
            null -> ArticleHolder_().id(-currentPosition)
            else -> ArticleHolder_().id(item.id).article(item)
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        super.addModels(models)
        NetworkStateHolder_()
            .id("network_state_footer")
            .networkState(networkState)
            .onRetryClick { retryCallback() }
            .addIf(networkState != NetworkState.LOADED, this)
    }
}