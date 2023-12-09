package com.example.shubhampatelanmolsharma_mapd711_assignment4.data.pizza

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapd711project.data.hotel.Hotel
import com.example.mapd711project.data.hotel.HotelRepository
import com.example.mapd711project.data.previewImage.PreviewImage
import kotlinx.coroutines.launch

class HotelViewModel(private val hotelRepository: HotelRepository): ViewModel() {

    private val _hotelsLiveData = MutableLiveData<List<Hotel>>()
    val hotelsLiveData: LiveData<List<Hotel>>
        get() = _hotelsLiveData

    private val _hotelLiveData = MutableLiveData<Hotel>()
    val hotelLiveData: LiveData<Hotel>
        get() = _hotelLiveData

    fun insertHotels(hotels: List<Hotel>){
        viewModelScope.launch {
            hotelRepository.insertHotels(hotels)
        }
    }

    fun getAllHotels() {
        viewModelScope.launch {
            val hotels = hotelRepository.getAllHotels()
            _hotelsLiveData.postValue(hotels)
        }
    }

    fun getHotelWithId(hotelId: Int){
        viewModelScope.launch {
            val hotel = hotelRepository.getHotelById(hotelId)
            _hotelLiveData.value = hotel
        }
    }

    suspend fun insertPreviewImages(previewImages: List<PreviewImage>) {
        hotelRepository.insertPreviewImages(previewImages)
    }

    suspend fun getPreviewImagesByHotelId(hotelId: Int): List<PreviewImage> {
        return hotelRepository.getPreviewImagesByHotelId(hotelId)
    }
}