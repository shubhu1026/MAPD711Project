package com.example.mapd711project.data.booking

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val bookingId: Int = 0,
    val customerId: Int,
    val bookedBy: String,
    val phoneNumber: String,
    val hotelId: Int,
    val checkInDate: String,
    val checkOutDate: String,
    val rooms: Int,
    var status: String,
    val totalCost: Double,
)
