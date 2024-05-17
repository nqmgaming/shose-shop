package com.nqmgaming.shoseshop.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.adapter.viewpagger2.BannerAdapter
import com.nqmgaming.shoseshop.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bannerViewpager.adapter = BannerAdapter(
            listOf(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3
            )
        )
        binding.indicator.setViewPager2(binding.bannerViewpager)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}