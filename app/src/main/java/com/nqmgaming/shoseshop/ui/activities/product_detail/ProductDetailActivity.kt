package com.nqmgaming.shoseshop.ui.activities.product_detail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.type.InputRadioButtons
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.adapter.product.ProductAdapter
import com.nqmgaming.shoseshop.adapter.viewpagger2.ImageProductAdapter
import com.nqmgaming.shoseshop.data.model.main.cart.CartRequest
import com.nqmgaming.shoseshop.data.model.main.cart.ItemCartRequest
import com.nqmgaming.shoseshop.data.model.main.size.Size
import com.nqmgaming.shoseshop.data.model.main.size.SizeDetail
import com.nqmgaming.shoseshop.databinding.ActivityProductDetailBinding
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel by viewModels<ProductDetailViewModel>()
    private lateinit var size: Size
    private lateinit var sizeSelect: SizeDetail
    private lateinit var userId: String
    private var price by Delegates.notNull<Double>()
    private lateinit var productImage: String
    private lateinit var categoryId: String
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
        userId = SharedPrefUtils.getString(this, "id", "") ?: ""
        binding.apply {

            selectSizeBtn.setOnClickListener {
                if (::size.isInitialized) {
                    InputSheet().show(this@ProductDetailActivity) {
                        title("Select size")
                        with(InputRadioButtons("size") {
                            options(size.size.map { it.name }.toMutableList())
                            changeListener { value ->
                                sizeSelect = size.size[value]
                                selectSizeBtn.text = "Size ${size.name} ${sizeSelect.name}"
                            }
                        })

                    }
                }
            }

            addToBagBtn.setOnClickListener {
                if (::sizeSelect.isInitialized) {
                    val items = ItemCartRequest(
                        product = id,
                        size = sizeSelect.name,
                        quantity = 1,
                        price = price,
                    )
                    val cartRequest = CartRequest(
                        user = userId,
                        items = items
                    )
                    viewModel.createCart(token, cartRequest) {
                        if (it != null) {
                            PopupDialog.getInstance(this@ProductDetailActivity)
                                .setStyle(Styles.SUCCESS)
                                .setHeading("Add to bag success!")
                                .setDescription("Continue purchase")
                                .setDismissButtonText("Ok")
                                .setCancelable(false)
                                .showDialog(object : OnDialogButtonClickListener() {
                                    override fun onDismissClicked(dialog: Dialog) {
                                        super.onDismissClicked(dialog)
                                        dialog.dismiss()
                                    }
                                })
                        } else {
                            PopupDialog.getInstance(this@ProductDetailActivity)
                                .setStyle(Styles.FAILED)
                                .setHeading("Add to bag failed!")
                                .setDescription("Product is already in the cart")
                                .setDismissButtonText("Ok")
                                .setCancelable(false)
                                .showDialog(object : OnDialogButtonClickListener() {
                                    override fun onDismissClicked(dialog: Dialog) {
                                        super.onDismissClicked(dialog)
                                        dialog.dismiss()
                                    }
                                })
                        }
                    }
                } else {
                    InputSheet().show(this@ProductDetailActivity) {
                        title("Please select size first")
                        with(InputRadioButtons("size") {
                            options(size.size.map { it.name }.toMutableList())
                            changeListener { value ->
                                sizeSelect = size.size[value]
                                selectSizeBtn.text = "Size ${size.name} ${sizeSelect.name}"
                            }
                        })

                    }
                }
            }
        }


        viewModel.getProductDetail(token, id) { it ->
            if (it != null) {
                binding.apply {
                    productNameTv.text = it.name
                    productPriceTv.text = "$ ${it.price}"
                    productCategoryTv.text = it.category.name
                    productDsTv.text = it.description
                    productNameMainTv.text = it.name
                    ratingTv.text = it.rating.toString()
                    ratingBar.rating = it.rating.toFloat()
                    productStockTv.text = "Stock: ${it.stock}"
                    imageViewpager.adapter = ImageProductAdapter(it.images)
                    indicator.setViewPager2(imageViewpager)
                    size = it.size
                    selectSizeBtn.text = "Size ${size.name} ${size.size[0].name}"
                    price = it.price
                    productImage = it.imagePreview
                    categoryId = it.category.id
                    if (it.stock <= 0) {
                        binding.addToBagBtn.isEnabled = false
                        binding.addToBagBtn.text = "Out of stock"
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


    }
}