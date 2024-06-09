package com.nqmgaming.shoseshop.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nqmgaming.shoseshop.data.model.main.order.Order
import com.nqmgaming.shoseshop.databinding.ItemSumTotalBinding
import com.nqmgaming.shoseshop.ui.fragments.cart.CartViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class OrderTotalAdapter(
    private val viewModel: CartViewModel,
    private val token: String,
) : RecyclerView.Adapter<OrderTotalAdapter.OrderTotalViewHolder>() {

    inner class OrderTotalViewHolder(private val binding: ItemSumTotalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = originalFormat.parse(order.createdAt)
                val formattedDate = if (date != null) targetFormat.format(date) else ""
                binding.tvDay.text = formattedDate
                val formattedPrice =
                    NumberFormat.getNumberInstance(Locale.US).format(order.total.toInt())
                binding.tvTotalPrice.text = formattedPrice + " $"
                binding.itemOrderRecyclerView.setHasFixedSize(true)
                binding.itemOrderRecyclerView.adapter = OrderItemAdapter(
                    viewModel,
                    token
                ).apply {
                    differ.submitList(order.products)
                }

            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((Order) -> Unit)? = null

    fun setOnItemClickListener(listener: (Order) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderTotalViewHolder {
        val binding = ItemSumTotalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderTotalViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderTotalViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(order)
            }
        }
    }
}