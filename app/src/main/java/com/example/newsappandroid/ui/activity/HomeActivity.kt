package com.example.newsappandroid.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsappandroid.R
import com.example.newsappandroid.dao.ArticleDatabase
import com.example.newsappandroid.databinding.ActivityHomeBinding
import com.example.newsappandroid.repository.NewsRepository
import com.example.newsappandroid.ui.fragment.BreakingNewsFragment
import com.example.newsappandroid.ui.fragment.ProfileFragment
import com.example.newsappandroid.ui.fragment.SavedNewsFragment
import com.example.newsappandroid.ui.fragment.SearchNewsFragment
import com.example.newsappandroid.ui.viewmodel.NewViewModelProviderFactory
import com.example.newsappandroid.ui.viewmodel.NewsViewModel
import meow.bottomnavigation.MeowBottomNavigation


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewViewModelProviderFactory(application, newRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        replaceFragment(BreakingNewsFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.breakingNewsFragment -> replaceFragment(BreakingNewsFragment())
                R.id.savedNewsFragment -> replaceFragment(SavedNewsFragment())
                R.id.searchNewsFragment -> replaceFragment(SearchNewsFragment())
                R.id.profileFragment -> replaceFragment(ProfileFragment())

                else ->{



                }

            }

            true

        }


    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.newsNavHostFragment,fragment).commit()
    }
}