package com.example.mapd711project.data.hotel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HotelDao {
    @Insert
    suspend fun insertHotel(hotel: Hotel)

    @Insert
    suspend fun insertHotels(hotels: List<Hotel>)
    @Query("SELECT * FROM hotels")
    suspend fun getAllHotels(): List<Hotel>

    @Query("SELECT * FROM hotels WHERE hotelId = :hotelId")
    suspend fun getHotelById(hotelId: Int): Hotel
}