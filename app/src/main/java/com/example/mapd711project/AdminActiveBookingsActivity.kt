package com.example.mapd711project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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
import com.example.mapd711project.databinding.ActivityAdminActiveBookingsBinding
import com.example.mapd711project.rvAdapters.AdminActiveBookingsAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class AdminActiveBookingsActivity : AppCompatActivity(), AdminActiveBookingsAdapter.BookingDeleteListener {

    private lateinit var binding : ActivityAdminActiveBookingsBinding

    private lateinit var adminViewModel: AdminViewModel
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var activeBookingsAdapter: AdminActiveBookingsAdapter

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminActiveBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

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

            if (activeBookings.isEmpty()) {
                binding.noBookingsText.visibility = View.VISIBLE
                binding.rvActiveBookings.visibility = View.GONE
            } else {
                binding.noBookingsText.visibility = View.GONE
                binding.rvActiveBookings.visibility = View.VISIBLE
            }
        })

        bookingViewModel.getActiveBookings()

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        drawerLayout = binding.myDrawerLayout
        setupNavigationView()
        setupDrawer()

        binding.menuButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    override fun onDeleteClicked(booking: Booking) {
        lifecycleScope.launch {
            bookingViewModel.deleteBooking(booking)
            bookingViewModel.getActiveBookings()
            Toast.makeText(this@AdminActiveBookingsActivity, "Booking Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupNavigationView() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu
        navView.menu.clear()

        // Add custom menu items
        menu.add("Home")
        menu.add("Pending Requests")
        menu.add("Logout")

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.title) {
                "Home" -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                "Pending Requests" -> {
                    startActivity(Intent(this, AdminBookingRequestsActivity::class.java))
                    true
                }
                "Logout" -> {
                    logoutUser()
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun logoutUser() {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", "")
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }
}