package com.example.mapd711project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapd711project.databinding.ActivitySearchHotelsBinding
import com.example.mapd711project.rvAdapters.NearestHotelsAdapter
import com.example.mapd711project.rvAdapters.PopularHotelsAdapter

class SearchHotelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchHotelsBinding

    private val hotelList = listOf(
        Hotel("Hotel ABC", "New York", R.drawable.hotel_image_1),
        Hotel("Hotel XYZ", "Paris", R.drawable.hotel_image_2),
        Hotel("Hotel 123", "London", R.drawable.hotel_image_3),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchHotelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPopularHotel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularHotel.adapter = PopularHotelsAdapter(hotelList)

        binding.rvNearestHotel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNearestHotel.adapter = NearestHotelsAdapter(hotelList)
    }
}