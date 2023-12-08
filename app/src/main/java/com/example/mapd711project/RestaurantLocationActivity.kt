package com.example.mapd711project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mapd711project.databinding.ActivityRestaurantLocationBinding

class RestaurantLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}