package com.example.mypaging.main

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.mypaging.R
import com.example.mypaging.main.data.NetworkState
import com.example.mypaging.utils.KotlinEpoxyHolder
import timber.log.Timber

@EpoxyModelClass(layout = R.layout.network_state_item)
abstract class NetworkStateHolder: EpoxyModelWithHolder<NetworkStateHolder.ViewHolder>() {

    @EpoxyAttribute var networkState: NetworkState? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onRetryClick: () -> Unit

    override fun bind(holder: ViewHolder) {
        Timber.d("NetworkStateHolder bind networkState = $networkState")
        holder.progressBar.visibility = toVisibility(networkState?.status == NetworkState.Status.RUNNING)
        holder.retryButton.visibility = toVisibility(networkState?.status == NetworkState.Status.FAILED)
        holder.retryButton.setOnClickListener { onRetryClick() }
        holder.errorMsg.visibility = toVisibility(networkState?.msg != null)
        holder.errorMsg.text = networkState?.msg
    }

    override fun unbind(holder: ViewHolder) {
        super.unbind(holder)
        holder.retryButton.setOnClickListener(null)
    }

    companion object {
        fun toVisibility(constraint : Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    class ViewHolder: KotlinEpoxyHolder() {
        val errorMsg by bind<TextView>(R.id.error_msg)
        val progressBar by bind<ProgressBar>(R.id.progress_bar)
        val retryButton by bind<Button>(R.id.retry_button)
    }
}