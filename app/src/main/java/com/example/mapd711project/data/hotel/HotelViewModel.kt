package com.example.shubhampatelanmolsharma_mapd711_assignment4.data.pizza

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapd711project.data.hotel.Hotel
import com.example.mapd711project.data.hotel.HotelRepository
import kotlinx.coroutines.launch

class HotelViewModel(private val hotelRepository: HotelRepository): ViewModel() {
    private val _hotels = MutableLiveData<List<Hotel>>()
    private val _fetchedHotel = MutableLiveData<Hotel?>()

    val fetchedHotel: LiveData<Hotel?>
        get() = _fetchedHotel
    val hotels: LiveData<List<Hotel>>
        get() = _hotels

    fun getHotels() {
        viewModelScope.launch {
            val result = hotelRepository.getAllHotels()
            _hotels.value = result
        }
    }

    fun getHotelByName(hotelName: String){
        viewModelScope.launch {
            val result = hotelRepository.getHotelByName(hotelName)
            _fetchedHotel.value = result
        }
    }

    suspend fun getHotelIdFromHotelName(hotelName: String): Int? {
        return hotelRepository.getHotelIdFromHotelName(hotelName)
    }

    fun insertHotel(hotel: Hotel) {
        viewModelScope.launch {
            hotelRepository.insertHotel(hotel)
        }
    }

    fun insertHotels(hotels: List<Hotel>){
        viewModelScope.launch {
            hotelRepository.insertHotels(hotels)
        }
    }

    fun updateHotel(hotel: Hotel) {
        viewModelScope.launch {
            hotelRepository.updateHotel(hotel)
        }
    }

    fun deleteHotel(hotel: Hotel) {
        viewModelScope.launch {
            hotelRepository.deleteHotel(hotel)
        }
    }

    fun deleteAllHotels() {
        viewModelScope.launch {
            hotelRepository.deleteAllHotels()
        }
    }
}