package com.example.mapd711project.data.hotel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shubhampatelanmolsharma_mapd711_assignment4.data.pizza.HotelViewModel

class HotelViewModelFactory(private val hotelRepository: HotelRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HotelViewModel::class.java)) {
            return HotelViewModel(hotelRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}