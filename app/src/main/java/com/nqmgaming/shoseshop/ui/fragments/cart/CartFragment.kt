package com.nqmgaming.shoseshop.ui.fragments.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.adapter.cart.CartAdapter
import com.nqmgaming.shoseshop.data.model.product.Product
import com.nqmgaming.shoseshop.databinding.FragmentCartBinding
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CartViewModel>()
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

        viewModel.getAllCartsMain(bearerToken, userId) { carts ->
            if (carts != null) {
                binding.cartRv.adapter = CartAdapter(viewModel, bearerToken).apply {
                    differ.submitList(carts)
                    setOnItemClickListener {
                        // Handle item click
                    }
                    setOnMinusClickListener { cart ->
                        viewModel.updateQuantityMain(
                            bearerToken,
                            cart.id,
                            mapOf("quantity" to cart.items.quantity - 1)
                        ) { cart ->
                            if (cart != null) {
                                viewModel.getAllCartsMain(bearerToken, userId) {
                                    if (it != null) {
                                        differ.submitList(it)
                                        binding.totalPriceTv.text =
                                            it.sumByDouble { cart -> cart.items.price * cart.items.quantity }
                                                .toString()
                                    }
                                }
                            }
                        }
                    }
                    setOnPlusClickListener { cart ->
                        viewModel.updateQuantityMain(
                            bearerToken,
                            cart.id,
                            mapOf("quantity" to cart.items.quantity + 1)
                        ) { cart ->
                            if (cart != null) {
                                viewModel.getAllCartsMain(bearerToken, userId) {
                                    if (it != null) {
                                        differ.submitList(it)
                                        binding.totalPriceTv.text =
                                            it.sumByDouble { cart -> cart.items.price * cart.items.quantity }
                                                .toString()
                                    }
                                }
                            }
                        }
                    }
                    setOnDeleteListener { cart ->
                        viewModel.deleteCartMain(bearerToken, cart.id) {
                            viewModel.getAllCartsMain(bearerToken, userId) {
                                if (it != null) {
                                    differ.submitList(it)
                                    binding.totalPriceTv.text =
                                        it.sumByDouble { cart -> cart.items.price * cart.items.quantity }
                                            .toString()
                                    Toast.makeText(
                                        requireContext(),
                                        "Item deleted successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }

                binding.llCartEmpty.visibility =
                    if (carts.isEmpty()) View.VISIBLE else View.GONE
                binding.cartRv.visibility = if (carts.isEmpty()) View.GONE else View.VISIBLE
                binding.totalPriceTv.text =
                    carts.sumByDouble { cart -> cart.items.price * cart.items.quantity }
                        .toString()

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}