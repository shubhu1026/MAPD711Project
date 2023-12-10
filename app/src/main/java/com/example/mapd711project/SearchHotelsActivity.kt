package com.example.mapd711project

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.hotel.HotelRepository
import com.example.mapd711project.data.hotel.HotelViewModel
import com.example.mapd711project.data.hotel.HotelViewModelFactory
import com.example.mapd711project.databinding.ActivitySearchHotelsBinding
import com.example.mapd711project.rvAdapters.PopularHotelsAdapter
import com.example.mapd711project.rvAdapters.RecommendedHotelsAdapter
import com.google.android.material.navigation.NavigationView


class SearchHotelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchHotelsBinding
    private lateinit var recommendedHotelsAdapter: RecommendedHotelsAdapter
    private lateinit var popularHotelsAdapter: PopularHotelsAdapter

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchHotelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val database = AppDatabase.getDatabase(applicationContext)

        val hotelRepository = HotelRepository(database.hotelDao(), database.previewImageDao())
        val hotelViewModelFactory = HotelViewModelFactory(hotelRepository)
        hotelViewModel = ViewModelProvider(this, hotelViewModelFactory)[HotelViewModel::class.java]

        recommendedHotelsAdapter = RecommendedHotelsAdapter(emptyList())
        popularHotelsAdapter = PopularHotelsAdapter(emptyList())

        binding.rvPopularHotel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularHotel.adapter = popularHotelsAdapter

        binding.rvRecommendedHotel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvRecommendedHotel.adapter = recommendedHotelsAdapter

        hotelViewModel.getPopularHotels()
        hotelViewModel.getRecommendedHotels()

        hotelViewModel.popularHotelsLiveData.observe(this, Observer{hotels->
            hotels?.let{
                popularHotelsAdapter.updateHotelList(it)
            }
        })

        hotelViewModel.recommendedHotelsLiveData.observe(this, Observer{hotels->
            hotels?.let{
                recommendedHotelsAdapter.updateHotelList(it)
            }
        })

        hotelViewModel.getAllHotels()

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
            logoutUser()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupNavigationView() {
        val navView: NavigationView = findViewById(com.example.mapd711project.R.id.nav_view)
        val menu = navView.menu
        navView.menu.clear()

        // Add new custom menu items
        menu.add("Home")
        menu.add("My Profile")
        menu.add("My Bookings")
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
                "My Bookings" -> {
                    startActivity(Intent(this, MyBookingsActivity::class.java))
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
            com.example.mapd711project.R.string.nav_open,
            com.example.mapd711project.R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
}