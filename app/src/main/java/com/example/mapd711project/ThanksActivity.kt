package com.example.mapd711project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.mapd711project.databinding.ActivityPaymentBinding
import com.example.mapd711project.databinding.ActivityThanksBinding
import com.google.android.material.navigation.NavigationView

class ThanksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThanksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThanksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seeBookingsBsutton.setOnClickListener {
            startActivity(Intent(this, MyBookingsActivity::class.java))
        }
    }
}