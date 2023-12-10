package com.example.mapd711project

import android.R
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
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


class SearchHotelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchHotelsBinding
    private lateinit var recommendedHotelsAdapter: RecommendedHotelsAdapter
    private lateinit var popularHotelsAdapter: PopularHotelsAdapter

    private lateinit var hotelViewModel: HotelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchHotelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    /*
    private fun showCustomMenu(anchorView: View) {
        // Inflate the custom menu layout
        val popupView: View = LayoutInflater.from(this).inflate(R.layout, null)

        // Create a PopupWindow and set its properties
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set a transparent background
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set focusable to true to receive touch events
        popupWindow.isFocusable = true

        // Show the popup menu at a specific position relative to the anchor view
        popupWindow.showAsDropDown(anchorView) // Replace this with showAtLocation() if needed

        // Handle clicks on menu items
        val menuItem1 = popupView.findViewById<TextView>(R.id.menuItem1)
        val menuItem2 = popupView.findViewById<TextView>(R.id.menuItem2)
        menuItem1.setOnClickListener { v: View? ->
            // Handle click on menuItem1
            // Example: perform an action or dismiss the popup
            popupWindow.dismiss()
        }
        menuItem2.setOnClickListener { v: View? ->
            // Handle click on menuItem2
            // Example: perform an action or dismiss the popup
            popupWindow.dismiss()
        }

        // Add click listeners for other menu items similarly
    }
g
     */
}