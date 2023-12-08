package com.example.mapd711project.rvAdapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapd711project.Hotel
import com.example.mapd711project.HotelDetailsActivity
import com.example.mapd711project.R

class NearestHotelsAdapter(private val hotelList: List<Hotel>) : RecyclerView.Adapter<NearestHotelsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nearest_hotels, parent, false)
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

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(itemView.context, HotelDetailsActivity::class.java)
                    intent.putExtra("hotel_Name", hotelList[position].name)
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bind(hotel: Hotel) {
            hotelImage.setImageResource(hotel.imageResource) // Use hotel image resource here
            hotelName.text = hotel.name // Use hotel name here
            hotelAddress.text = hotel.location
        }
    }
}
