package com.example.mapd711project.data.hotel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotels")
data class Hotel(
    @PrimaryKey(autoGenerate = true) val productId: Int = 0,
    val hotelName: String,
    val price: Double,
    val category: String
)