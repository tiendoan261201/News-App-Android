package com.example.newsappandroid.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappandroid.R
import com.example.newsappandroid.adapter.NewsAdapter
import com.example.newsappandroid.databinding.FragmentSearchNewsBinding
import com.example.newsappandroid.ui.activity.HomeActivity
import com.example.newsappandroid.ui.viewmodel.NewsViewModel
import com.example.newsappandroid.utils.Constants.QUERY_PAGE_SIZE
import com.example.newsappandroid.utils.Constants.SEARCH_NEWS_TIME_DELAY
import com.example.newsappandroid.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : BaseFragment<FragmentSearchNewsBinding>() {
    private lateinit var viewModel: NewsViewModel
    val TAG = "searchNewsFragment"
    lateinit var newsAdapter: NewsAdapter

    override fun initview() {
        viewModel = (activity as HomeActivity).viewModel
        setUpRecycleView()
        setItemClickAdapter()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults/ QUERY_PAGE_SIZE*2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if(isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"An error occured: $message", Toast.LENGTH_SHORT).show()
                        showErrorMessage(message)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    override fun initData() {

    }

    override fun initListener() {
        binding.itemErrorMessage.btnRetry.setOnClickListener{
            if(binding.etSearch.text.toString().isNotEmpty()){
                viewModel.searchNews(binding.etSearch.text.toString())

            }else{
                hideErrorMessage()
            }
        }

    }

    override fun getViewBing(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchNewsBinding {
        return FragmentSearchNewsBinding.inflate(inflater, container, false)

    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
    }
    private fun hideErrorMessage() {
        binding.itemErrorMessage.root.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        binding.itemErrorMessage.root.visibility = View.VISIBLE
        binding.itemErrorMessage.tvErrorMessage.text = message
        isError = true
    }


    private fun setUpRecycleView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setItemClickAdapter() {
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            loadFragment(ArticleFragment(), bundle)
        }
    }

    private fun loadFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.newsNavHostFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


}

