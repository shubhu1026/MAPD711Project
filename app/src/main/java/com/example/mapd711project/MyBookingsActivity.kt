package com.example.mapd711project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.booking.BookingRepository
import com.example.mapd711project.data.booking.BookingViewModel
import com.example.mapd711project.data.booking.BookingViewModelFactory
import com.example.mapd711project.data.customer.CustomerRepository
import com.example.mapd711project.data.customer.CustomerViewModel
import com.example.mapd711project.data.customer.CustomerViewModelFactory
import com.example.mapd711project.data.hotel.HotelRepository
import com.example.mapd711project.data.hotel.HotelViewModel
import com.example.mapd711project.data.hotel.HotelViewModelFactory
import com.example.mapd711project.databinding.ActivityMyBookingsBinding
import com.example.mapd711project.databinding.ActivityPaymentBinding
import com.example.mapd711project.rvAdapters.MyBookingsAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MyBookingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyBookingsBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var bookingsAdapter: MyBookingsAdapter

    private var customerId: Int = -1

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(applicationContext)
        val customerRepository = CustomerRepository(database.customerDao())
        val customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelFactory)[CustomerViewModel::class.java]

        val bookingRepository = BookingRepository(database.bookingDao())
        val bookingViewModelFactory = BookingViewModelFactory(bookingRepository)
        bookingViewModel = ViewModelProvider(this, bookingViewModelFactory)[BookingViewModel::class.java]

        val hotelRepository = HotelRepository(database.hotelDao(), database.previewImageDao())
        val hotelViewModelFactory = HotelViewModelFactory(hotelRepository)
        hotelViewModel = ViewModelProvider(this, hotelViewModelFactory)[HotelViewModel::class.java]

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        binding.rvHotelBookings.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bookingsAdapter = MyBookingsAdapter(emptyList(), hotelViewModel)
        binding.rvHotelBookings.adapter = bookingsAdapter

        bookingViewModel.bookingsLiveData.observe(this, Observer { bookings ->
            bookingsAdapter.updateBookingsList(bookings)
        })

        if(isUserLoggedIn()){
            val email = getUserEmail()
            getUserIDFromDatabase(email)
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

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }
    private fun getUserEmail(): String {
        return sharedPreferences.getString("user_email", "") ?: ""
    }

    private fun getUserIDFromDatabase(email: String) {

        lifecycleScope.launch {
            val customer = customerViewModel.getCustomerByEmail(email)
            if (customer != null) {
                customerId = customer.customerId
                fetchBookingsForCustomer(customerId)
            }else {
                showToast("Something went Wrong")
            }
        }
    }

    private fun fetchBookingsForCustomer(customerId: Int) {
        bookingViewModel.getBookingsWithCustomerId(customerId)
        bookingViewModel.bookingsLiveData.observe(this, Observer { bookings ->
            if (bookings != null && bookings.isNotEmpty()) {
                bookingsAdapter.updateBookingsList(bookings)
                binding.noBookingsText.visibility = View.GONE
                binding.rvHotelBookings.visibility = View.VISIBLE
            } else {
                showToast("No bookings found")
                binding.noBookingsText.visibility = View.VISIBLE
                binding.rvHotelBookings.visibility = View.GONE
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupNavigationView() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu
        navView.menu.clear()

        // Add new custom menu items
        menu.add("Home")
        menu.add("My Profile")
        menu.add("Search Hotels")
        menu.add("Logout")

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.title) {
                "Home" -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                "My Profile" -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                "Search Hotels" -> {
                    startActivity(Intent(this, SearchHotelsActivity::class.java))
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

    private fun logoutUser() {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", "")
        editor.putBoolean("is_logged_in", true)
        editor.apply()
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
}