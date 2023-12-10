package com.example.mapd711project.data.booking

import androidx.lifecycle.LiveData
import com.example.mapd711project.data.customer.Customer

class BookingRepository(private val bookingDao: BookingDao) {
    suspend fun insertBooking(booking: Booking) {
        bookingDao.insertBooking(booking)
    }

    suspend fun getBookingsByCustomerId(customerId: Int): List<Booking> {
        return bookingDao.getBookingsByCustomerId(customerId)
    }

}