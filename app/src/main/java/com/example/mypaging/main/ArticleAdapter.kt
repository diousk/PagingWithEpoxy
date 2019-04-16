package com.example.mypaging.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mypaging.R
import com.example.mypaging.databinding.ItemFeedBinding
import com.example.mypaging.main.data.NetworkState
import com.example.mypaging.model.Article
import timber.log.Timber

class ArticleAdapter(
    private val retryCallback: () -> Unit
): PagedListAdapter<Article, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_feed -> {
                val binding = ItemFeedBinding.inflate(layoutInflater, parent, false)
                ArticleHolder(binding)
            }
            R.layout.network_state_item -> {
                NetworkStateHolder(layoutInflater.inflate(R.layout.network_state_item, parent, false), retryCallback)
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            R.layout.item_feed -> (holder as ArticleHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateHolder).bind(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.item_feed
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        Timber.d("setNetworkState hasExtraRow=$hasExtraRow, hasExtraRow=$hasExtraRow, itemCount=$itemCount")
        Timber.d("setNetworkState previousState=$previousState, newNetworkState=$newNetworkState")
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.title == newItem.title

            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id
        }
    }
}