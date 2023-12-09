package com.example.mapd711project.rvAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mapd711project.R
import com.example.mapd711project.data.previewImage.PreviewImage

class PreviewImagesAdapter(private val previewImages: List<PreviewImage>) : RecyclerView.Adapter<PreviewImagesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_image_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val previewImage = previewImages[position]
        holder.bind(previewImage)
    }

    override fun getItemCount(): Int {
        return previewImages.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(previewImage: PreviewImage) {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.hotel_image) // Placeholder if image loading fails

            Glide.with(itemView.context)
                .load(previewImage.imagePath)
                .apply(requestOptions)
                .into(imageView)
        }
    }
}
