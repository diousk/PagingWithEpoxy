package com.example.mypaging.main

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypaging.R
import com.example.mypaging.main.data.NetworkState
import com.example.mypaging.main.data.NetworkState.Status
import timber.log.Timber

class NetworkStateViewHolder(
    view: View, private val retryCallback: () -> Unit
): RecyclerView.ViewHolder(view) {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val retryButton = view.findViewById<Button>(R.id.retry_button)
    private val errorMsg = view.findViewById<TextView>(R.id.error_msg)

    init {
        retryButton.setOnClickListener { retryCallback() }
    }

    fun bind(networkState: NetworkState?) {
        Timber.d("bind networkState = $networkState")
        progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        retryButton.visibility = toVisibility(networkState?.status == Status.FAILED)
        errorMsg.visibility = toVisibility(networkState?.msg != null)
        errorMsg.text = networkState?.msg
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
}