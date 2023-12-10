package com.example.mapd711project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.hotel.HotelRepository
import com.example.mapd711project.data.hotel.HotelViewModel
import com.example.mapd711project.data.hotel.HotelViewModelFactory
import com.example.mapd711project.databinding.ActivityBookingSummaryBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookingSummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingSummaryBinding

    private lateinit var hotelViewModel: HotelViewModel

    private var hotelId: Int = -1
    private var roomCount: Int = -1
    private var checkInDate: String = ""
    private var checkOutDate: String = ""
    private var totalCost: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(applicationContext)
        val hotelRepository = HotelRepository(database.hotelDao(), database.previewImageDao())
        val hotelViewModelFactory = HotelViewModelFactory(hotelRepository)
        hotelViewModel = ViewModelProvider(this, hotelViewModelFactory).get(HotelViewModel::class.java)

        val intent = intent
        hotelId = intent.getIntExtra("hotelId", -1)
        checkInDate = intent.getStringExtra("checkInDate") ?: ""
        checkOutDate = intent.getStringExtra("checkOutDate") ?: ""
        roomCount = intent.getIntExtra("roomCount", 0)

        if (hotelId != -1) {
            setupBookingDetails()
        }

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

            Glide.with(this@BookingSummaryActivity) // Changed context to this@HotelDetailsActivity
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
}