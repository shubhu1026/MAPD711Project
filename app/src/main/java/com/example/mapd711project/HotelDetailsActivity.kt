package com.example.mapd711project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapd711project.databinding.ActivityHotelDetailsBinding
import com.example.mapd711project.rvAdapters.HotelImagesAdapter

class HotelDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHotelDetailsBinding

    private val imageList = listOf(R.drawable.hotel_image_1, R.drawable.hotel_image_2, R.drawable.hotel_image_3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvHotelImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHotelImages.adapter = HotelImagesAdapter(imageList)

        binding.bookButton.setOnClickListener {
            startActivity(Intent(this, BookingDetailsActivity::class.java))
        }
    }
}