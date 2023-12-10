package com.example.mapd711project.rvAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mapd711project.R
import com.example.mapd711project.data.booking.Booking
import com.example.mapd711project.data.hotel.HotelViewModel

class MyBookingsAdapter(
    private var bookings: List<Booking>,
    private val hotelViewModel: HotelViewModel
) : RecyclerView.Adapter<MyBookingsAdapter.MyViewHolder>() {

    fun updateBookingsList(newBookings: List<Booking>) {
        bookings = newBookings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hotel_booking, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount(): Int {
        return bookings.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hotelNameTextView: TextView = itemView.findViewById(R.id.hotelName)
        private val hotelAddressTextView: TextView = itemView.findViewById(R.id.hotelAddress)
        private val bookedByText: TextView = itemView.findViewById(R.id.bookedByText)
        private val contactNoText: TextView = itemView.findViewById(R.id.contactNoText)
        private val checkInText: TextView = itemView.findViewById(R.id.checkInText)
        private val checkOutText: TextView = itemView.findViewById(R.id.checkOutText)
        private val roomsText: TextView = itemView.findViewById(R.id.roomsText)
        private val totalText: TextView = itemView.findViewById(R.id.totalText)
        private val starCountTextView: TextView = itemView.findViewById(R.id.starCount)
        private val hotelImageView: ImageView = itemView.findViewById(R.id.hotelImage)

        fun bind(booking: Booking) {
            hotelViewModel.getHotelWithId(booking.hotelId)
            hotelViewModel.hotelLiveData.observeForever { hotel ->
                hotel?.let {
                    hotelNameTextView.text = it.hotelName
                    hotelAddressTextView.text = it.address
                    starCountTextView.text = it.rating

                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.hotel_image) // Placeholder if image loading fails

                    Glide.with(itemView.context)
                        .load(hotel.displayImage) // Loading image from assets
                        .apply(requestOptions)
                        .into(hotelImageView)// Use hotel image resource here
                }
            }

            bookedByText.text = booking.bookedBy
            contactNoText.text = booking.phoneNumber
            checkInText.text = booking.checkInDate
            checkOutText.text = booking.checkOutDate
            roomsText.text = booking.rooms.toString()
            totalText.text = "$${booking.totalCost}"
        }
    }
}
