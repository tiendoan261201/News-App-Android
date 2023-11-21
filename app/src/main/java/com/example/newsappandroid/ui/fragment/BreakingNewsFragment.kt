package com.example.newsappandroid.ui.fragment


import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappandroid.R
import com.example.newsappandroid.adapter.NewsAdapter
import com.example.newsappandroid.databinding.FragmentBreakingNewsBinding
import com.example.newsappandroid.ui.activity.HomeActivity
import com.example.newsappandroid.ui.viewmodel.NewsViewModel
import com.example.newsappandroid.utils.Constants.QUERY_PAGE_SIZE
import com.example.newsappandroid.utils.Resource


class BreakingNewsFragment : BaseFragment<FragmentBreakingNewsBinding>() {
    private lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "BreakingNewsFragment"
    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    override fun initview() {
        viewModel =(activity as HomeActivity).viewModel
        setUpRecycleView()
        setItemClickAdapter()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                    newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults/QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage ==totalPages
                        if(isLastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let { message->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.itemErrorMessage.btnRetry.setOnClickListener {
            viewModel.getBreakingNews("us")
        }
    }

    override fun getViewBing(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBreakingNewsBinding {
        return FragmentBreakingNewsBinding.inflate(inflater,container,false)

    }
    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.GONE
        isLoading = false
    }
    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
    private fun hideErrorMessage(){
        binding.itemErrorMessage.root.visibility = View.GONE
        isError = false
    }
    private fun showErrorMessage(message: String){
        binding.itemErrorMessage.root.visibility = View.VISIBLE
        binding.itemErrorMessage.tvErrorMessage.text = message
        isError = true
    }

    private fun setUpRecycleView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    private fun setItemClickAdapter(){
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
           loadFragment(ArticleFragment(),bundle)
        }
    }
    private fun loadFragment(fragment: Fragment, bundle: Bundle){
        fragment.arguments = bundle
        val transaction =  requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.newsNavHostFragment,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
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
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


}