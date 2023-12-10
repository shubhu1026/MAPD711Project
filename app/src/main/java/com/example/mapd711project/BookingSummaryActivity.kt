package com.example.mapd711project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.customer.CustomerRepository
import com.example.mapd711project.data.customer.CustomerViewModel
import com.example.mapd711project.data.customer.CustomerViewModelFactory
import com.example.mapd711project.data.hotel.HotelRepository
import com.example.mapd711project.data.hotel.HotelViewModel
import com.example.mapd711project.data.hotel.HotelViewModelFactory
import com.example.mapd711project.databinding.ActivityBookingSummaryBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookingSummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingSummaryBinding

    private lateinit var hotelViewModel: HotelViewModel
    private lateinit var customerViewModel: CustomerViewModel

    private var hotelId: Int = -1
    private var roomCount: Int = -1
    private var checkInDate: String = ""
    private var checkOutDate: String = ""
    private var totalCost: Double = 0.0

    private var hotelName: String = "Hotel Details"

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = applicationContext.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val database = AppDatabase.getDatabase(applicationContext)
        val hotelRepository = HotelRepository(database.hotelDao(), database.previewImageDao())
        val hotelViewModelFactory = HotelViewModelFactory(hotelRepository)
        hotelViewModel = ViewModelProvider(this, hotelViewModelFactory).get(HotelViewModel::class.java)

        val customerRepository = CustomerRepository(AppDatabase.getDatabase(applicationContext).customerDao())
        val customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelFactory)[CustomerViewModel::class.java]

        val intent = intent
        hotelName = intent.getStringExtra("hotelName") ?: "Hotel Details"
        hotelId = intent.getIntExtra("hotelId", -1)
        checkInDate = intent.getStringExtra("checkInDate")?.trim() ?: ""
        checkOutDate = intent.getStringExtra("checkOutDate")?.trim() ?: ""
        roomCount = intent.getIntExtra("roomCount", 0)

        if (hotelId != -1) {
            setupBookingDetails()
        }

        fetchUserDetailsAndAutofillFields()

        binding.bookButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val phone = binding.phoneNoInput.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show()
            } else if (phone.isEmpty() || phone.length < 10) {
                Toast.makeText(this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show()
            } else {
                val myIntent = Intent(this, PaymentActivity::class.java)

                myIntent.putExtra("hotelId", hotelId)
                myIntent.putExtra("bookedBy",name)
                myIntent.putExtra("phone", phone)
                myIntent.putExtra("checkInDate", checkInDate)
                myIntent.putExtra("checkOutDate", checkOutDate)
                myIntent.putExtra("roomCount", roomCount)
                myIntent.putExtra("totalCost", totalCost)


                startActivity(myIntent)
            }
        }

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

    private fun fetchUserDetailsAndAutofillFields() {
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("is_logged_in", false)
        val email: String = sharedPreferences.getString("user_email", "").toString()

        if(!isLoggedIn)
        {
            return
        }

        lifecycleScope.launch {
            val customer = customerViewModel.getCustomerByEmail(email)
            if (customer != null) {
                binding.nameInput.setText(customer.name)
                binding.phoneNoInput.setText(customer.phone)
            }else {
                Toast.makeText(this@BookingSummaryActivity,"Something went Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupBookingDetails() {
        hotelViewModel.getHotelWithId(hotelId)
        hotelViewModel.hotelLiveData.observe(this) { hotel ->
            binding.hotelName.text = hotel.hotelName
            binding.starCount.text = hotel.rating
            binding.hotelAddress.text = hotel.address

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.hotel_image) // Placeholder if image loading fails

            Glide.with(this@BookingSummaryActivity)
                .load(hotel.displayImage) // Loading image from assets
                .apply(requestOptions)
                .into(binding.hotelImage)

            val checkIn: String = checkInDate.trim()
            val checkOut: String = checkOutDate.trim()

            binding.checkInText.text = checkIn
            binding.checkOutText.text = checkOut
            binding.roomsText.text = roomCount.toString()

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val checkInDateObj = sdf.parse(checkInDate)
            val checkOutDateObj = sdf.parse(checkOutDate)

            if (checkInDateObj != null && checkOutDateObj != null) {
                val diffInMillies = checkOutDateObj.time - checkInDateObj.time
                val diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
                totalCost = diffInDays * hotel.price * roomCount

                binding.totalText.text = "$$totalCost"
            } else {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNavigationView() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu
        navView.menu.clear()

        // Add new custom menu items
        menu.add("Home")
        menu.add("Profile")
        menu.add("Search Hotels")
        menu.add(hotelName)
        menu.add("Booking Details")
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

                hotelName -> {
                    val intent =
                        Intent(this@BookingSummaryActivity, HotelDetailsActivity::class.java)
                    intent.putExtra("hotelId", hotelId)
                    intent.putExtra("hotelName", hotelName)
                    startActivity(intent)
                    true
                }

                "Booking Details" -> {
                    val intent =
                        Intent(this@BookingSummaryActivity, BookingDetailsActivity::class.java)
                    intent.putExtra("hotelId", hotelId)
                    intent.putExtra("hotelName", hotelName)
                    startActivity(intent)
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