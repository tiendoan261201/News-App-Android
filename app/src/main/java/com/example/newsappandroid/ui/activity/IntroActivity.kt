package com.example.newsappandroid.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.newsappandroid.R
import com.example.newsappandroid.adapter.ViewPagerAdapter
import com.example.newsappandroid.databinding.ActivityIntroBinding
import com.example.newsappandroid.ui.fragment.FragmentIntro

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var pagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityIntroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initListener()
    }

    private fun initView() {
        pagerAdapter = ViewPagerAdapter(supportFragmentManager, 0)
        pagerAdapter.addFragment(
            FragmentIntro.newInstance(
                R.raw.on_boarding_one,
                R.string.Welcome_to,
                R.string.tv_intro_2
            ),""
        )
        pagerAdapter.addFragment(
            FragmentIntro.newInstance(
                R.raw.on_boarding_two,
                R.string.tv_intro_3,
                R.string.tv_intro_4
            ),""
        )
        pagerAdapter.addFragment(
            FragmentIntro.newInstance(
                R.raw.on_boarding_three,
                R.string.tv_intro_5,
                R.string.tv_intro_6
            ),""
        )
        binding.slideViewPager.adapter = pagerAdapter
        binding.slideViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 2){
                    binding.btnNext.text = getString(R.string.done)
                }else{
                    binding.btnNext.text = getString(R.string.btn_next_tv)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
        binding.indicate.attachTo(binding.slideViewPager)
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            if(binding.slideViewPager.currentItem == 2){
                val intent = Intent(this@IntroActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                binding.slideViewPager.currentItem += 1
            }
        }
        binding.btnBack.setOnClickListener {
            if (binding.slideViewPager.currentItem > 0){
                binding.slideViewPager.currentItem -= 1
            }
        }
        binding.tvSkip.setOnClickListener {
            val intent = Intent(this@IntroActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}