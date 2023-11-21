package com.example.newsappandroid.ui.fragment


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.newsappandroid.databinding.FragmentIntroBinding

class FragmentIntro : BaseFragment<FragmentIntroBinding>() {


    override fun initview() {
        val image = arguments?.getInt("image") ?: 0
        val txt1 = arguments?.getInt("txt1") ?: 0
        val txt2 = arguments?.getInt("txt2") ?: 0

        binding.introVIew.setAnimation(image)
        binding.texttitle.text = if (txt1 == 0) {
            ""
        } else {
            getString(txt1)
        }
        binding.textdeccription.text = if (txt2 == 0) {
            ""
        } else {
            getString(txt2)
        }

    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun getViewBing(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentIntroBinding {
        return FragmentIntroBinding.inflate(layoutInflater, container, false)
    }

    companion object {
        fun newInstance(image: Int, txt1: Int, txt2: Int): FragmentIntro {
             return FragmentIntro().also {
                it.arguments = bundleOf(
                    "image" to image,
                    "txt1" to txt1,
                    "txt2" to txt2
                )
            }
        }
    }


}