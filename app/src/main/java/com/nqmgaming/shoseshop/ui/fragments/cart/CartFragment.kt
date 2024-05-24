package com.nqmgaming.shoseshop.ui.fragments.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.adapter.cart.CartAdapter
import com.nqmgaming.shoseshop.data.model.cart.Cart
import com.nqmgaming.shoseshop.data.model.product.Product
import com.nqmgaming.shoseshop.databinding.FragmentCartBinding
import com.nqmgaming.shoseshop.ui.activities.checkout.CheckoutActivity
import com.nqmgaming.shoseshop.ui.activities.productDetail.ProductDetailActivity
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CartViewModel>()
    private lateinit var categoryId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = SharedPrefUtils.getString(requireContext(), "accessToken", "") ?: ""
        val bearerToken = "Bearer $token"
        val userId = SharedPrefUtils.getString(requireContext(), "id", "") ?: ""

        binding.paymentBtn.setOnClickListener {
            Intent(requireContext(), CheckoutActivity::class.java).apply {
                startActivity(this)
            }
        }

        viewModel.getAllCartsMain(bearerToken, userId) { carts ->
            if (carts != null) {
                binding.cartRv.adapter = CartAdapter(viewModel, bearerToken).apply {
                    differ.submitList(carts)
                    setOnItemClickListener { handleItemClick(it, bearerToken) }
                    setOnMinusClickListener { handleQuantityChange(it, bearerToken, userId, -1) }
                    setOnPlusClickListener { handleQuantityChange(it, bearerToken, userId, 1) }
                    setOnDeleteListener { handleItemDeletion(it, bearerToken, userId) }
                }
                updateUI(carts)
            }
        }

    }

    private fun handleItemClick(cart: Cart, bearerToken: String) {
        Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra("id", cart.items.product)
            putExtra("token", bearerToken)
            startActivity(this)
        }
    }

    private fun handleQuantityChange(cart: Cart, bearerToken: String, userId: String, change: Int) {
        viewModel.updateQuantityMain(
            bearerToken,
            cart.id,
            mapOf("quantity" to cart.items.quantity + change)
        ) { updatedCart ->
            if (updatedCart != null) {
                refreshCartList(bearerToken, userId)
            }
        }
    }

    private fun handleItemDeletion(cart: Cart, bearerToken: String, userId: String) {
        viewModel.deleteCartMain(bearerToken, cart.id) {
            refreshCartList(bearerToken, userId)
            Toast.makeText(
                requireContext(),
                "Item deleted successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun refreshCartList(bearerToken: String, userId: String) {
        viewModel.getAllCartsMain(bearerToken, userId) { updatedCarts ->
            (binding.cartRv.adapter as CartAdapter).differ.submitList(updatedCarts)
            updateUI(updatedCarts ?: emptyList())
        }
    }

    private fun updateUI(carts: List<Cart>) {
        val isEmpty = carts.isEmpty()
        binding.llCartEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.cartRv.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.totalPriceTv.text =
            "$: ${carts.sumOf { cart -> cart.items.price * cart.items.quantity }}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}