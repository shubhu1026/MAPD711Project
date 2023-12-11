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
import com.example.mapd711project.databinding.ActivityAdminActiveRequestsBinding
import com.example.mapd711project.rvAdapters.AdminActiveBookingsAdapter
import kotlinx.coroutines.launch

class AdminActiveBookingsActivity : AppCompatActivity(), AdminActiveBookingsAdapter.BookingDeleteListener {

    private lateinit var binding : ActivityAdminActiveRequestsBinding

    private lateinit var adminViewModel: AdminViewModel
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var activeBookingsAdapter: AdminActiveBookingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminActiveRequestsBinding.inflate(layoutInflater)
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

        binding.rvActiveBookings.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        activeBookingsAdapter = AdminActiveBookingsAdapter(emptyList(), hotelViewModel, this)
        binding.rvActiveBookings.adapter = activeBookingsAdapter

        bookingViewModel.activeBookingsLiveData.observe(this, Observer { activeBookings ->
            activeBookingsAdapter.updateActiveBookingsList(activeBookings)
        })

        bookingViewModel.getActiveBookings()
    }

    override fun onDeleteClicked(booking: Booking) {
        lifecycleScope.launch {
            bookingViewModel.deleteBooking(booking)
            bookingViewModel.getActiveBookings()
        }
    }
}