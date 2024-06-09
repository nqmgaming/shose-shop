package com.nqmgaming.shoseshop.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nqmgaming.shoseshop.data.model.main.order.OrderProduct
import com.nqmgaming.shoseshop.data.model.main.product.Product
import com.nqmgaming.shoseshop.databinding.ItemOrderBinding
import com.nqmgaming.shoseshop.ui.fragments.cart.CartViewModel
import java.text.NumberFormat
import java.util.Locale

class OrderItemAdapter(
    private val viewModel: CartViewModel,
    private val token: String,
) : RecyclerView.Adapter<OrderItemAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            product: Product,
            quantity: Int
        ) {
            binding.apply {
                binding.tvQuantityItemOrder.text = "Quantity: $quantity"
                Glide.with(binding.root.context)
                    .load(product.imagePreview)
                    .into(binding.ivItemOrder)
                binding.tvNameItemOrder.text = product.name
                binding.tvCategoryItemOrder.text = product.category.name
                val formattedPrice =
                    NumberFormat.getNumberInstance(Locale.US).format(product.price.toInt())
                binding.tvPriceItemOrder.text = "Price: ${formattedPrice} $"

            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<OrderProduct>() {
        override fun areItemsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((OrderProduct) -> Unit)? = null

    fun setOnItemClickListener(listener: (OrderProduct) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val productId = differ.currentList[position].productId
        val quantity = differ.currentList[position].quantity
        viewModel.getProductDetailMain(token, productId) { product ->
            if (product != null) {
                holder.bind(product, quantity)
            }
        }

    }
}