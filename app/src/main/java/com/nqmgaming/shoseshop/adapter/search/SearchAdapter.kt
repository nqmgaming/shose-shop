package com.nqmgaming.shoseshop.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nqmgaming.shoseshop.data.model.main.product.Product
import com.nqmgaming.shoseshop.databinding.ItemSearchProductBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val binding: ItemSearchProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                productNameTv.text = product.name
                productPriceTv.text = product.price.toString() + " USD"
                productCategoryTv.text = product.category.name ?: ""
            }
            Glide.with(binding.root).load(product.imagePreview).into(binding.ivProduct)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(product)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }

}