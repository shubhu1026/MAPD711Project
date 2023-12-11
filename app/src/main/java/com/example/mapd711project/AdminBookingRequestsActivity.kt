package com.example.mapd711project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.admin.AdminRepository
import com.example.mapd711project.data.admin.AdminViewModel
import com.example.mapd711project.data.admin.AdminViewModelFactory
import com.example.mapd711project.data.booking.Booking
import com.example.mapd711project.data.booking.BookingRepository
import com.example.mapd711project.data.booking.BookingViewModel
import com.example.mapd711project.data.booking.BookingViewModelFactory
import com.example.mapd711project.data.hotel.HotelRepository
import com.example.mapd711project.data.hotel.HotelViewModel
import com.example.mapd711project.data.hotel.HotelViewModelFactory
import com.example.mapd711project.databinding.ActivityAdminBookingRequestsBinding
import com.example.mapd711project.rvAdapters.AdminBookingRequestsAdapter
import kotlinx.coroutines.launch

class AdminBookingRequestsActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityAdminBookingRequestsBinding

    private lateinit var adminViewModel: AdminViewModel
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var bookingRequestsAdapter: AdminBookingRequestsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBookingRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)

        val adminRepository = AdminRepository(database.adminDao())
        val adminViewModelFactory = AdminViewModelFactory(adminRepository)
        adminViewModel = ViewModelProvider(this, adminViewModelFactory)[AdminViewModel::class.java]

        val bookingRepository = BookingRepository(database.bookingDao())
        val bookingViewModelFactory = BookingViewModelFactory(bookingRepository)
        bookingViewModel = ViewModelProvider(this, bookingViewModelFactory)[BookingViewModel::class.java]

        val hotelRepository = HotelRepository(database.hotelDao(), database.previewImageDao())
        val hotelViewModelFactory = HotelViewModelFactory(hotelRepository)
        hotelViewModel = ViewModelProvider(this, hotelViewModelFactory)[HotelViewModel::class.java]

        setupRecyclerViewAdapters()
    }

    private fun setupRecyclerViewAdapters(){
        binding.rvBookingRequests.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bookingRequestsAdapter = AdminBookingRequestsAdapter(
            emptyList(),
            hotelViewModel,
            object : AdminBookingRequestsAdapter.BookingOnAcceptListener {
                override fun onAcceptClicked(booking: Booking) {
                    // Handle accept action here
                    lifecycleScope.launch {
                        bookingViewModel.deleteBooking(booking)
                        bookingViewModel.getActiveBookings()
                    }
                }
            },
            object : AdminBookingRequestsAdapter.BookingOnDeclineListener {
                override fun onDeclineClicked(booking: Booking) {
                    lifecycleScope.launch {
                        val updatedBooking = booking.copy(status = "active")
                        bookingViewModel.updateBooking(updatedBooking)
                        bookingViewModel.getActiveBookings()
                    }
                }
            })

        binding.rvBookingRequests.adapter = bookingRequestsAdapter

        bookingViewModel.bookingRequestsLiveData.observe(this, Observer { bookingRequests ->
            bookingRequestsAdapter.updateBookingsRequestsList(bookingRequests)
        })

        bookingViewModel.getBookingRequests()
    }
}