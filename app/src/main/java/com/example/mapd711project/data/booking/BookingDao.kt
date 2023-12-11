package com.example.mapd711project.data.booking

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mapd711project.data.customer.Customer

@Dao
interface BookingDao {
    @Insert
    suspend fun insertBooking(booking: Booking)

    @Query("SELECT * FROM bookings WHERE customerId = :customerId")
    suspend fun getBookingsByCustomerId(customerId: Int): List<Booking>

    @Query("SELECT * FROM bookings WHERE status='active'")
    suspend fun getActiveBookings(): List<Booking>

    @Query("SELECT * FROM bookings WHERE status='pendingRequest'")
    suspend fun getBookingRequests(): List<Booking>

    @Update
    suspend fun updateBooking(booking: Booking)

    @Delete
    suspend fun deleteBooking(booking: Booking)
}