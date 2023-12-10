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
}