package com.nqmgaming.shoseshop.adapter.viewpagger2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.nqmgaming.shoseshop.R

class BannerAdapter(private val images: List<Int>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }
}