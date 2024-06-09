package com.nqmgaming.shoseshop.ui.activities.order_detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.ActivityOrderDetailBinding
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private val viewModel by viewModels<OrderDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val orderId = intent.getStringExtra("orderId") ?: ""
        val token = "Bearer " + SharedPrefUtils.getString(this, "accessToken", "")

        viewModel.getOrderByIdDetail(token, orderId, callback = { order ->
            if (order != null) {
                binding.orderId.text = order.id
                binding.orderPlaced.text = order.createdAt
                binding.total.text = order.total.toString() + " USD"
                binding.paymentMethod.text = order.paymentMethod
                binding.address.text = order.address
                binding.phone.text = order.phoneNumber
                binding.email.text = order.email
                binding.trackOrder.setOnClickListener {
                    //show current order status
                    //alert dialog
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Order Status")
                    alertDialog.setMessage("Your order is ${order.status}")
                    alertDialog.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialog.show()
                }
            } else {
                // Handle error
            }
        })
    }
}