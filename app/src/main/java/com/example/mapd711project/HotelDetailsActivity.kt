package com.example.mapd711project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.hotel.Hotel
import com.example.mapd711project.data.hotel.HotelRepository
import com.example.mapd711project.data.hotel.HotelViewModel
import com.example.mapd711project.data.hotel.HotelViewModelFactory
import com.example.mapd711project.databinding.ActivityHotelDetailsBinding
import com.example.mapd711project.rvAdapters.PreviewImagesAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class HotelDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHotelDetailsBinding
    private var hotelId: Int = -1
    private var hotelName: String = ""
    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val intent = intent
        hotelId = intent.getIntExtra("hotelId", -1)
        hotelName = intent.getStringExtra("hotelName").toString()

        val database = AppDatabase.getDatabase(applicationContext)
        val hotelRepository = HotelRepository(database.hotelDao(), database.previewImageDao())
        val hotelViewModelFactory = HotelViewModelFactory(hotelRepository)
        hotelViewModel = ViewModelProvider(this, hotelViewModelFactory).get(HotelViewModel::class.java)

        binding.rvPreviewImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        if (hotelId != -1) {
            setupHotelDetails()
        }

        binding.bookButton.setOnClickListener {
            val intent = Intent(this, BookingDetailsActivity::class.java)
            intent.putExtra("hotelId", hotelId)
            intent.putExtra("hotelName", hotelName)
            startActivity(intent)
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

    private fun setupHotelDetails() {
        hotelViewModel.getHotelWithId(hotelId)
        hotelViewModel.hotelLiveData.observe(this) { hotel ->
            binding.hotelName.text = hotel.hotelName
            binding.hotelNameTitle.text = hotel.hotelName
            binding.hotelLocation.text = hotel.location
            binding.starCount.text = hotel.rating
            binding.hotelDescription.text = hotel.hotelDescription
            binding.price.text = "$${hotel.price}"

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.hotel_image) // Placeholder if image loading fails

            Glide.with(this@HotelDetailsActivity) // Changed context to this@HotelDetailsActivity
                .load(hotel.displayImage) // Loading image from assets
                .apply(requestOptions)
                .into(binding.hotelImage)

            // You can also fetch and display associated preview images if needed
            fetchAndDisplayPreviewImages(hotelId)
        }
    }

    private fun fetchAndDisplayPreviewImages(hotelId: Int) {
        lifecycleScope.launch {
            val previewImages = hotelViewModel.getPreviewImagesByHotelId(hotelId)
            val adapter = PreviewImagesAdapter(previewImages)
            binding.rvPreviewImages.adapter = adapter
        }
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
