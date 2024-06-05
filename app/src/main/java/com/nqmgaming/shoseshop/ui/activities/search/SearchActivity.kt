package com.nqmgaming.shoseshop.ui.activities.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.adapter.search.SearchAdapter
import com.nqmgaming.shoseshop.databinding.ActivitySearchBinding
import com.nqmgaming.shoseshop.ui.activities.product_detail.ProductDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val token = intent.getStringExtra("token") ?: ""

        binding.backIv.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // When the user submits the query, search for the products
                if (!query.isNullOrEmpty()) {
                    viewModel.searchProductsSearch(token, query) { products ->

                        // If the products are not null, show them in the recycler view
                        products?.let { productList ->

                            val searchAdapter = SearchAdapter()
                            searchAdapter.differ.submitList(productList)
                            binding.rcSearch.adapter = searchAdapter

                            // show hide empty view
                            if (productList.isEmpty()) {
                                binding.llSearchEmpty.visibility = View.VISIBLE
                                binding.rcSearch.visibility = View.GONE
                            } else {
                                binding.llSearchEmpty.visibility = View.GONE
                                binding.rcSearch.visibility = View.VISIBLE
                            }

                            searchAdapter.setOnItemClickListener { product ->
                                Intent(
                                    this@SearchActivity,
                                    ProductDetailActivity::class.java
                                ).also {
                                    it.putExtra("id", product.id)
                                    it.putExtra("token", token)
                                    startActivity(it)
                                }

                            }

                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // You can also handle text changes here if you want to provide live search results
                return true
            }
        })
    }

}