package com.example.mapd711project.data.hotel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HotelViewModelFactory(private val hotelRepository: HotelRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HotelViewModel::class.java)) {
            return HotelViewModel(hotelRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}