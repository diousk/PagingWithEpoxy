package com.example.mypaging.main.view

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.mypaging.R
import com.example.mypaging.model.Article
import com.example.mypaging.utils.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_feed)
abstract class ArticleHolder: EpoxyModelWithHolder<ArticleHolder.ViewHolder>() {

    @EpoxyAttribute lateinit var article: Article

    override fun bind(holder: ViewHolder) {
        holder.titleView.text = article.title
    }

    override fun unbind(holder: ViewHolder) {
        super.unbind(holder)
    }

    class ViewHolder: KotlinEpoxyHolder() {
        val titleView by bind<TextView>(R.id.title)
    }
}