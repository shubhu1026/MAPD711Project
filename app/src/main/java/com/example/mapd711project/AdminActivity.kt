package com.example.mapd711project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mapd711project.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activeBookingsButton.setOnClickListener {
            startActivity(Intent(this, AdminActiveBookingsActivity::class.java))
        }

        binding.bookingRequestsButton.setOnClickListener {
            startActivity(Intent(this, AdminBookingRequestsActivity::class.java))
        }
    }
}