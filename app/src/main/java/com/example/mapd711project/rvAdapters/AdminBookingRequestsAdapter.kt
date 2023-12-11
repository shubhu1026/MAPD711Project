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
import com.example.mapd711project.data.hotel.HotelViewModel

class AdminBookingRequestsAdapter(private var bookingRequests: List<Booking>,
                                  private val hotelViewModel: HotelViewModel,
                                  private val acceptListener: BookingOnAcceptListener,
                                  private val declineListener: BookingOnDeclineListener) : RecyclerView.Adapter<AdminBookingRequestsAdapter.ViewHolder>() {

    fun updateBookingsRequestsList(newBookingRequests: List<Booking>) {
        bookingRequests = newBookingRequests
        notifyDataSetChanged()
    }

    interface BookingOnDeclineListener {
        fun onDeclineClicked(booking: Booking)
    }

    interface BookingOnAcceptListener {
        fun onAcceptClicked(booking: Booking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your item layout and create ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_requests_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookingRequest = bookingRequests[position]
        holder.bind(bookingRequest)
    }

    override fun getItemCount(): Int {
        return bookingRequests.size
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
        private val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        private val declineButton: Button = itemView.findViewById(R.id.declineButton)
        private val hotelImageView: ImageView = itemView.findViewById(R.id.hotelImage)

        fun bind(bookingRequest: Booking) {
            hotelViewModel.getHotelWithId(bookingRequest.hotelId)
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
            bookedByText.text = bookingRequest.bookedBy
            contactNoText.text = bookingRequest.phoneNumber
            checkInText.text = bookingRequest.checkInDate
            checkOutText.text = bookingRequest.checkOutDate
            roomsText.text = bookingRequest.rooms.toString()
            totalText.text = "$${bookingRequest.totalCost}"

            acceptButton.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val bookingToAccept = bookingRequests[currentPosition]
                    acceptListener.onAcceptClicked(bookingToAccept)
                }
            }

            declineButton.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val bookingToDecline = bookingRequests[currentPosition]
                    declineListener.onDeclineClicked(bookingToDecline)
                }
            }
        }
    }

}
