package com.nqmgaming.shoseshop.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.adapter.category.CategoryAdapter
import com.nqmgaming.shoseshop.adapter.product.ProductAdapter
import com.nqmgaming.shoseshop.adapter.viewpagger2.BannerAdapter
import com.nqmgaming.shoseshop.databinding.FragmentHomeBinding
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
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
        binding.productRv.layoutManager = (GridLayoutManager(requireContext(), 2))

        val token = SharedPrefUtils.getString(requireContext(), "accessToken", "") ?: ""
        val bearerToken = "Bearer $token"
        viewModel.getCategoriesHome(bearerToken) {
            if (it != null) {
                binding.categoryRv.adapter = CategoryAdapter().apply {
                    differ.submitList(it)
                    setOnItemClickListener {
                        // Handle item click
                    }
                    isDataLoaded = true
                    notifyItemChanged(selectedPosition)
                }
            }
        }
        viewModel.getProductsHome(bearerToken) {
            if (it != null) {
                binding.productRv.adapter = ProductAdapter().apply {
                    differ.submitList(it)
                    setOnItemClickListener {
                        // Handle item click
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}