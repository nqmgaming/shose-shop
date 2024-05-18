package com.nqmgaming.shoseshop.ui.activities.productDetail

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.adapter.product.ProductAdapter
import com.nqmgaming.shoseshop.adapter.viewpagger2.ImageProductAdapter
import com.nqmgaming.shoseshop.databinding.ActivityProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel by viewModels<ProductDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val id = intent.getStringExtra("id") ?: ""
        val token = intent.getStringExtra("token") ?: ""
        val categoryId = intent.getStringExtra("categoryId") ?: ""
        viewModel.getProductDetail(token, id) {
            if (it != null) {
                binding.apply {
                    productNameTv.text = it.name
                    productPriceTv.text = it.price.toString()
                    productCategoryTv.text = it.category.name
                    productDsTv.text = it.description
                    productNameMainTv.text = it.name
                    ratingTv.text = it.rating.toString()
                    ratingBar.rating = it.rating.toFloat()
                    productStockTv.text = "Stock: ${it.stock}"
                    imageViewpager.adapter = ImageProductAdapter(it.images)
                    indicator.setViewPager2(imageViewpager)
                }

            }
        }

        viewModel.getProductsByCategory(token, categoryId) {
            if (it != null) {
                binding.relatedProductsRv.adapter = ProductAdapter().apply {
                    differ.submitList(it)
                    setOnItemClickListener {
                        Intent(
                            this@ProductDetailActivity,
                            ProductDetailActivity::class.java
                        ).apply {
                            putExtra("id", it.id)
                            putExtra("token", token)
                            putExtra("categoryId", it.category.id)
                            startActivity(this)
                        }
                    }
                }
            }
        }

    }
}