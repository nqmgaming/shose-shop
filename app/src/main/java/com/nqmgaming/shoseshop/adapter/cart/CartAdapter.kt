package com.nqmgaming.shoseshop.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nqmgaming.shoseshop.data.model.main.cart.Cart
import com.nqmgaming.shoseshop.databinding.ItemCartBinding
import com.nqmgaming.shoseshop.ui.fragments.cart.CartViewModel

class CartAdapter(private val viewModel: CartViewModel, private val token: String) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart) {
            var productStock = 0
            viewModel.getProductDetailMain(token = token, cart.items.product) {
                if (it != null) {
                    binding.apply {
                        Glide.with(itemView)
                            .load(it.imagePreview)
                            .into(ivProductImage)
                        tvProductName.text = it.name
                        tvProductPrice.text = "$ ${it.price}"
                        tvProductSize.text = "Size ${cart.items.size}"
                        tvProductTotalPrice.text = "Total price: ${it.price * cart.items.quantity}"
                    }
                    productStock = it.stock

                }

            }
            binding.quantityTv.text = cart.items.quantity.toString()
            binding.apply {
                minusBtn.setOnClickListener {
                    if (cart.items.quantity > 1) {
                        binding.quantityTv.text = cart.items.quantity.toString()
                        onMinusClickListener?.let { it(cart) }
                    } else {
                        cart.items.quantity = 1
                        binding.quantityTv.text = cart.items.quantity.toString()
                        onDeleteListener?.let { it(cart) }
                    }
                }
                addBtn.setOnClickListener {
                    if (cart.items.quantity < productStock) {
                        binding.quantityTv.text = cart.items.quantity.toString()
                        onPlusClickListener?.let { it(cart) }
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Product stock is not enough",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = differ.currentList[position]
        holder.bind(cart)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(cart)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((Cart) -> Unit)? = null
    private var onMinusClickListener: ((Cart) -> Unit)? = null
    private var onPlusClickListener: ((Cart) -> Unit)? = null
    private var onDeleteListener: ((Cart) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cart) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnMinusClickListener(listener: (Cart) -> Unit) {
        onMinusClickListener = listener
    }

    fun setOnPlusClickListener(listener: (Cart) -> Unit) {
        onPlusClickListener = listener
    }

    fun setOnDeleteListener(listener: (Cart) -> Unit) {
        onDeleteListener = listener
    }
}