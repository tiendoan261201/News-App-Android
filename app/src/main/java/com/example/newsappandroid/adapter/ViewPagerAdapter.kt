package com.example.newsappandroid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.newsappandroid.model.PageModel

class ViewPagerAdapter(fm:FragmentManager, behavior:Int) :FragmentPagerAdapter(fm,behavior) {
    private val pageModels: MutableList<PageModel> = mutableListOf()

    fun addFragment(fragment: Fragment?, title:String?){
        val model = PageModel(fragment,title)
        pageModels.add(model)
    }

    override fun getCount(): Int {
       return pageModels.size
    }

    override fun getItem(position: Int): Fragment {
       return pageModels[position].fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageModels[position].title
    }
}