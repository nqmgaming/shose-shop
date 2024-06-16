package com.nqmgaming.shoseshop.ui.fragments.home

import android.content.Intent
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
import com.nqmgaming.shoseshop.data.model.main.category.Category
import com.nqmgaming.shoseshop.databinding.FragmentHomeBinding
import com.nqmgaming.shoseshop.ui.activities.product_detail.ProductDetailActivity
import com.nqmgaming.shoseshop.ui.activities.search.SearchActivity
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var categoryId: String
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

        binding.searchBar.setOnClickListener {
            Intent(requireContext(), SearchActivity::class.java).also {
                it.putExtra("token", bearerToken)
                startActivity(it)
            }
        }

        viewModel.getCategoriesHome(bearerToken) { categories ->
            if (categories != null) {
                binding.categoryRv.adapter = CategoryAdapter().apply {

                    // add category item all
                    val allCategory = Category("0", "All")
                    val allCategories = mutableListOf<Category>()
                    allCategories.add(allCategory)

                    differ.submitList(allCategories + categories)
                    setOnItemClickListener { category ->
                        categoryId = category.id
                        if (categoryId == "0") {
                            viewModel.getProductsHome(bearerToken) { products ->
                                if (products != null) {
                                    binding.productRv.adapter = ProductAdapter().apply {
                                        differ.submitList(products)
                                        setOnItemClickListener {
                                            // Intent to ProductDetailActivity
                                            val id = it.id
                                            intentToProductDetailActivity(
                                                id = id,
                                                bearerToken = bearerToken,
                                                categoryId = it.category.id
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            viewModel.getProductsByCategoryHome(
                                bearerToken,
                                categoryId
                            ) { products ->
                                if (products != null) {
                                    binding.productRv.adapter = ProductAdapter().apply {
                                        differ.submitList(products)
                                        setOnItemClickListener {
                                            // Intent to ProductDetailActivity
                                            val id = it.id
                                            intentToProductDetailActivity(
                                                id = id,
                                                bearerToken = bearerToken,
                                                categoryId = it.category.id
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    isDataLoaded = true
                    notifyItemChanged(selectedPosition)
                }
            }
        }
        viewModel.getProductsHome(bearerToken) { products ->
            if (products != null) {
                binding.productRv.adapter = ProductAdapter().apply {
                    differ.submitList(products)
                    setOnItemClickListener {
                        // Intent to ProductDetailActivity
                        val id = it.id
                        intentToProductDetailActivity(
                            id = id,
                            bearerToken = bearerToken,
                            categoryId = it.category.id
                        )
                    }
                }
            }
        }

    }


    private fun intentToProductDetailActivity(id: String, bearerToken: String, categoryId: String) {
        Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra("id", id)
            putExtra("token", bearerToken)
            putExtra("categoryId", categoryId)
            startActivity(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}