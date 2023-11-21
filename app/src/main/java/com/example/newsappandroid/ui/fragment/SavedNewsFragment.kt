package com.example.newsappandroid.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappandroid.R
import com.example.newsappandroid.adapter.NewsAdapter
import com.example.newsappandroid.databinding.FragmentSavedNewsBinding
import com.example.newsappandroid.ui.activity.HomeActivity
import com.example.newsappandroid.ui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class SavedNewsFragment : BaseFragment<FragmentSavedNewsBinding>() {
private lateinit var viewModel: NewsViewModel
lateinit var newsAdapter:NewsAdapter


    override fun initview() {
       viewModel = (activity as HomeActivity).viewModel
        setUpRecycleView()
        setItemClickAdapter()
        handleAddingAndDelete()
    }

    override fun initData() {
    }

    override fun initListener() {

    }

    override fun getViewBing(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSavedNewsBinding {
      return FragmentSavedNewsBinding.inflate(inflater,container,false)
    }
    private fun setUpRecycleView(){
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
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
    private fun loadFragment(fragment: Fragment,bundle: Bundle){
        fragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.newsNavHostFragment,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun handleAddingAndDelete(){
        val itemTouchHelperCallBack = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                view?.let {
                    Snackbar.make(it,"Successfully deleted article",Snackbar.LENGTH_SHORT).apply {
                        setAction("Undo"){
                            viewModel.saveArticle(article)
                        }
                        show()
                    }
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { article->
            newsAdapter.differ.submitList(article)
        })
    }



}