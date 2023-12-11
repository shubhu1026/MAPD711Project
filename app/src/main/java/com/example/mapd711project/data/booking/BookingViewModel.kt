package com.example.mapd711project.data.booking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapd711project.data.customer.Customer
import com.example.mapd711project.data.hotel.Hotel
import kotlinx.coroutines.launch

class BookingViewModel(private val bookingRepository: BookingRepository): ViewModel() {

    private val _bookingsLiveData = MutableLiveData<List<Booking>>()
    val bookingsLiveData: LiveData<List<Booking>>
        get() = _bookingsLiveData

    private val _activeBookingsLiveData = MutableLiveData<List<Booking>>()
    val activeBookingsLiveData: LiveData<List<Booking>>
        get() = _activeBookingsLiveData

    private val _bookingRequestsLiveData = MutableLiveData<List<Booking>>()
    val bookingRequestsLiveData: LiveData<List<Booking>>
        get() = _bookingRequestsLiveData

    suspend fun insertBooking(booking: Booking){
        bookingRepository.insertBooking(booking)
    }


    fun getBookingsWithCustomerId(customerId: Int) {
        viewModelScope.launch {
            val bookings = bookingRepository.getBookingsByCustomerId(customerId)
            _bookingsLiveData.postValue(bookings)
            Log.d("BookingViewModel", "Fetched bookings: $bookings")
        }
    }

    fun getActiveBookings() {
        viewModelScope.launch {
            val activeBookings = bookingRepository.getActiveBookings()
            _activeBookingsLiveData.postValue(activeBookings)
        }
    }

    fun getBookingRequests() {
        viewModelScope.launch {
            val bookingRequests = bookingRepository.getBookingRequests()
            _bookingRequestsLiveData.postValue(bookingRequests)
        }
    }

    suspend fun updateBooking(booking: Booking) {
        bookingRepository.updateBooking(booking)
    }

    suspend fun deleteBooking(booking: Booking){
        bookingRepository.deleteBooking(booking)
    }
}
