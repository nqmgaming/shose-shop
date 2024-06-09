package com.nqmgaming.shoseshop.ui.activities.order

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.data.model.main.order.Order
import com.nqmgaming.shoseshop.data.model.main.order.OrderProduct
import com.nqmgaming.shoseshop.databinding.ActivityOrderBinding
import com.nqmgaming.shoseshop.ui.activities.MainActivity
import com.nqmgaming.shoseshop.ui.fragments.cart.CartViewModel
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private val viewModel by viewModels<OrderViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    private var paymentMethod: String = "Cash"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val totalPrice = intent.getStringExtra("total") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val token = intent.getStringExtra("token") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val userId = SharedPrefUtils.getString(this, "id") ?: ""


        //get date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(LocalDate.now().toString(), formatter)

        binding.rdCash.isChecked = true

        binding.rdCc.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                paymentMethod = "CC"
                binding.rdCash.isChecked = false
                binding.rdPaypal.isChecked = false
            }
        }

        binding.rdCash.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                paymentMethod = "Cash"
                binding.rdCc.isChecked = false
                binding.rdPaypal.isChecked = false
            }
        }

        binding.rdPaypal.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                paymentMethod = "Paypal"
                binding.rdCash.isChecked = false
                binding.rdCc.isChecked = false
            }
        }

        binding.btnNext.setOnClickListener {
            // Get all carts from the server by userId and create an order
            cartViewModel.getAllCartsMain(token, userId) { carts ->
                if (carts != null) {
                    val order = Order(
                        id = "",
                        userId = userId,
                        email = email,
                        phoneNumber = phoneNumber,
                        address = address,
                        createdAt = date.toString(),
                        updatedAt = date.toString(),
                        status = "Pending",
                        paymentMethod = paymentMethod,
                        total = totalPrice.toDouble().toInt(),
                        products = carts.map { cart ->
                            OrderProduct(
                                id = cart.id,
                                productId = cart.items.product,
                                quantity = cart.items.quantity
                            )
                        }
                    )
                    Log.d("OrderActivity", "onCreate: $order")
                    viewModel.createOrderOrder(token, order) { isOrderCreated ->
                        if (isOrderCreated) {
                            // Order created successfully
                            // Update the stock for each product in the cart
                            carts.forEach { cart ->
                                viewModel.getProductStockOrder(token, cart.items.product) { stock ->
                                    val newStock = stock - cart.items.quantity
                                    val map = mapOf("stock" to newStock)
                                    viewModel.updateProductStockOrder(token, cart.items.product, map) { isStockUpdated ->
                                        if (!isStockUpdated) {
                                            Log.e("OrderActivity", "Error updating stock for product: ${cart.items.product}")
                                        }
                                    }
                                }
                            }
                            // Clear the cart
                            viewModel.deleteAllCartOrder(token, userId) { done ->
                                if (done) {
                                    PopupDialog.getInstance(this@OrderActivity)
                                        .setStyle(Styles.SUCCESS)
                                        .setHeading("Order created successfully!")
                                        .setDescription("Thanks for your purchase")
                                        .setDismissButtonText("OK")
                                        .setCancelable(false)
                                        .showDialog(object : OnDialogButtonClickListener() {
                                            override fun onDismissClicked(dialog: Dialog) {
                                                super.onDismissClicked(dialog)
                                                dialog.dismiss()
                                                Intent(
                                                    this@OrderActivity,
                                                    MainActivity::class.java
                                                ).also { intent ->
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                            }
                                        })
                                } else {
                                    Toast.makeText(this, "Error deleting cart", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Error creating order", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}