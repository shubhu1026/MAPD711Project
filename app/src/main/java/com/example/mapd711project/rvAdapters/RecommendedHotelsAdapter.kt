package com.example.mapd711project.rvAdapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mapd711project.HotelDetailsActivity
import com.example.mapd711project.R
import com.example.mapd711project.data.hotel.Hotel

class RecommendedHotelsAdapter(private var hotelList: List<Hotel>) : RecyclerView.Adapter<RecommendedHotelsAdapter.ViewHolder>() {
    fun updateHotelList(newHotelList: List<Hotel>) {
        hotelList = newHotelList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommended_hotels, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotel = hotelList[position]
        holder.bind(hotel)
    }

    override fun getItemCount(): Int {
        return hotelList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hotelImage: ImageView = itemView.findViewById(R.id.hotelImage)
        private val hotelName: TextView = itemView.findViewById(R.id.hotelName)
        private val hotelAddress: TextView = itemView.findViewById(R.id.hotelAddress)
        private val hotelRating: TextView = itemView.findViewById(R.id.starCount)
        private val hotelPrice: TextView = itemView.findViewById(R.id.hotelPrice)
        private val context = itemView.context

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(itemView.context, HotelDetailsActivity::class.java)
                    intent.putExtra("hotelId", hotelList[position].hotelId)
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bind(hotel: Hotel) {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.hotel_image) // Placeholder if image loading fails

            Glide.with(context)
                .load(hotel.displayImage) // Loading image from assets
                .apply(requestOptions)
                .into(hotelImage)// Use hotel image resource here
            hotelName.text = hotel.hotelName // Use hotel name here
            hotelAddress.text = hotel.location
            hotelRating.text = hotel.rating
            hotelPrice.text = "$${hotel.price}/night"
        }
    }
}
