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

    //read users
    @Query("SELECT * FROM hotels")
    suspend fun getAllHotels(): List<Hotel>

    @Query("SELECT * FROM hotels WHERE hotelName = :hotelName")
    suspend fun getHotelByName(hotelName: String): Hotel?

    //update user
    @Update
    suspend fun updateHotel(hotel: Hotel)

    //delete user
    @Delete
    suspend fun deleteHotel(hotel: Hotel)

    //deleteAll
    @Query("DELETE FROM hotels")
    suspend fun deleteAllHotels()
}