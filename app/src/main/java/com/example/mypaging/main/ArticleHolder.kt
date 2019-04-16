package com.example.mypaging.main

import androidx.recyclerview.widget.RecyclerView
import com.example.mypaging.databinding.ItemFeedBinding
import com.example.mypaging.model.Article

class ArticleHolder(
    private val binding: ItemFeedBinding
): RecyclerView.ViewHolder(binding.rootView) {
    fun bind(article: Article?) {
        binding.title.text = article?.title
    }
}