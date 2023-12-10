package com.example.mapd711project.data.hotel

import android.location.Address
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotels")
data class Hotel(
    @PrimaryKey(autoGenerate = true) val hotelId: Int = 0,
    val hotelName: String,
    val category: String,
    val price: Double,
    val location: String,
    val address: String,
    val rating: String,
    val hotelDescription: String,
    val displayImage: String,
)