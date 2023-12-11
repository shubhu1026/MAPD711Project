package com.example.mapd711project.rvAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mapd711project.R
import com.example.mapd711project.data.booking.Booking
import com.example.mapd711project.data.booking.BookingViewModel
import com.example.mapd711project.data.hotel.HotelViewModel

class AdminActiveBookingsAdapter(private var activeBookings: List<Booking>,
                                 private val hotelViewModel: HotelViewModel,
                                 private val deleteListener: BookingDeleteListener ) : RecyclerView.Adapter<AdminActiveBookingsAdapter.ViewHolder>() {

    interface BookingDeleteListener {
        fun onDeleteClicked(booking: Booking)
    }

    fun updateActiveBookingsList(newActiveBookings: List<Booking>) {
        activeBookings = newActiveBookings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your item layout and create ViewHolder
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_active_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activeBooking = activeBookings[position]
        holder.bind(activeBooking)
    }

    override fun getItemCount(): Int {
        return activeBookings.size
    }

    // Inner ViewHolder class to hold your views
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hotelNameTextView: TextView = itemView.findViewById(R.id.hotelName)
        private val starCountTextView: TextView = itemView.findViewById(R.id.starCount)
        private val hotelAddressTextView: TextView = itemView.findViewById(R.id.hotelAddress)
        private val bookedByText: TextView = itemView.findViewById(R.id.bookedByText)
        private val contactNoText: TextView = itemView.findViewById(R.id.contactNoText)
        private val checkInText: TextView = itemView.findViewById(R.id.checkInText)
        private val checkOutText: TextView = itemView.findViewById(R.id.checkOutText)
        private val roomsText: TextView = itemView.findViewById(R.id.roomsText)
        private val totalText: TextView = itemView.findViewById(R.id.totalText)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        private val hotelImageView: ImageView = itemView.findViewById(R.id.hotelImage)

        fun bind(activeBooking: Booking) {
            hotelViewModel.getHotelWithId(activeBooking.hotelId)
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
            bookedByText.text = activeBooking.bookedBy
            contactNoText.text = activeBooking.phoneNumber
            checkInText.text = activeBooking.checkInDate
            checkOutText.text = activeBooking.checkOutDate
            roomsText.text = activeBooking.rooms.toString()
            totalText.text = "$${activeBooking.totalCost}"

            deleteButton.setOnClickListener {
                // Handle accept button click
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val bookingToDelete = activeBookings[currentPosition]
                    deleteListener.onDeleteClicked(bookingToDelete)
                }
            }
        }
    }
}
