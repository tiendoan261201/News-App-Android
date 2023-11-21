package com.example.newsappandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsappandroid.R
import com.example.newsappandroid.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivArticleImage:ImageView = itemView.findViewById(R.id.ivArticleImage)
        val tvSource:TextView = itemView.findViewById(R.id.tvSource)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription:TextView = itemView.findViewById(R.id.tvDescription)
        val tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_articles, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articles = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(articles.urlToImage).into(holder.ivArticleImage)
            holder.tvSource.text = articles.source.name
            holder.tvTitle.text = articles.title
            holder.tvDescription.text = articles.description
            setOnClickListener{
                onItemClickListener?.let { it(articles) }
            }

        }
    }
    private var onItemClickListener:((Article)->Unit)? = null
    fun setOnItemClickListener(listener: (Article)->Unit){
        onItemClickListener = listener
    }

}