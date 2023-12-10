package com.example.mapd711project

import android.R
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
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
}