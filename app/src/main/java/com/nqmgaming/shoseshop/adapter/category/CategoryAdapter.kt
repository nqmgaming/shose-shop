package com.nqmgaming.shoseshop.adapter.category

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nqmgaming.shoseshop.data.model.main.category.Category
import com.nqmgaming.shoseshop.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var selectedPosition = 0
    var isDataLoaded = false

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.apply {
                categoryName.text = category.name
                if (isDataLoaded) {
                    if (adapterPosition == selectedPosition) {
                        categoryName.setTypeface(null, Typeface.BOLD)
                    } else {
                        categoryName.setTypeface(null, Typeface.NORMAL)
                    }
                }
                categoryItem.setOnClickListener {
                    onItemClickListener?.let { it(category) }
                    selectedPosition = adapterPosition
                    notifyDataSetChanged()
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.bind(category)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((Category) -> Unit)? = null

    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }
}