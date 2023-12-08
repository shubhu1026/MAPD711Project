package com.example.mapd711project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.customer.CustomerRepository
import com.example.mapd711project.data.customer.CustomerViewModel
import com.example.mapd711project.data.customer.CustomerViewModelFactory
import com.example.mapd711project.data.hotel.HotelRepository
import com.example.mapd711project.data.hotel.HotelViewModelFactory
import com.example.mapd711project.databinding.ActivitySearchHotelsBinding
import com.example.mapd711project.rvAdapters.NearestHotelsAdapter
import com.example.mapd711project.rvAdapters.PopularHotelsAdapter
import com.example.shubhampatelanmolsharma_mapd711_assignment4.data.pizza.HotelViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchHotelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchHotelsBinding
    private lateinit var nearestHotelsAdapter: NearestHotelsAdapter
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

        nearestHotelsAdapter = NearestHotelsAdapter(emptyList())
        popularHotelsAdapter = PopularHotelsAdapter(emptyList())

        binding.rvPopularHotel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularHotel.adapter = popularHotelsAdapter

        binding.rvNearestHotel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNearestHotel.adapter = nearestHotelsAdapter

        hotelViewModel.hotelsLiveData.observe(this, Observer { hotels ->
            hotels?.let {
                nearestHotelsAdapter.updateHotelList(it)
                popularHotelsAdapter.updateHotelList(it)
            }
        })

        hotelViewModel.getAllHotels()
    }
}