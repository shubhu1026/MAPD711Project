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

    suspend fun getActiveBookings(): List<Booking> {
        return bookingDao.getActiveBookings()
    }

    suspend fun getBookingRequests(): List<Booking> {
        return bookingDao.getBookingRequests()
    }

    suspend fun updateBooking(booking: Booking){
        return bookingDao.updateBooking(booking)
    }

    suspend fun deleteBooking(booking: Booking) {
        bookingDao.deleteBooking(booking)
    }

}