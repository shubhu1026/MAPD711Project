package com.example.mapd711project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.coroutines.launch

class HotelDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHotelDetailsBinding
    private var hotelId: Int = -1
    private lateinit var hotelViewModel: HotelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        hotelId = intent.getIntExtra("hotelId", -1)

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
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
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
}
