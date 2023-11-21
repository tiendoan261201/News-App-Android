package com.example.newsappandroid.ui.fragment

import   android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsappandroid.R
import com.example.newsappandroid.databinding.FragmentArticleBinding
import com.example.newsappandroid.model.Article
import com.example.newsappandroid.ui.activity.HomeActivity
import com.example.newsappandroid.ui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : BaseFragment<FragmentArticleBinding>() {
    lateinit var viewModel: NewsViewModel
    override fun initview() {
        viewModel = (activity as HomeActivity).viewModel



    }

    override fun initData() {

    }

    override fun initListener() {
        val article = arguments?.getSerializable("article") as? Article
        Log.e("article","${article.toString().isNullOrEmpty()}")
        if (article != null){
            binding.webView.apply {
                settings.javaScriptEnabled = true
                settings.setSupportZoom(true)
                webViewClient = WebViewClient()
                loadUrl(article.url)
            }
        }

        binding.fab.setOnClickListener{
            if (article != null) {
                viewModel.saveArticle(article)
                view?.let { it1 -> Snackbar.make(it1,"Article was saved succesfully",Snackbar.LENGTH_SHORT).show() }
            }
        }
    }

    override fun getViewBing(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentArticleBinding {
        return FragmentArticleBinding.inflate(inflater,container,false)
    }


}