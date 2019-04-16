package com.example.mypaging.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mypaging.databinding.ItemFeedBinding
import com.example.mypaging.model.Article

class ArticalAdapter(
    private val retryCallback: () -> Unit
): PagedListAdapter<Article, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedBinding.inflate(layoutInflater, parent, false)
        return ArticleHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArticleHolder).bind(getItem(position)!!)
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