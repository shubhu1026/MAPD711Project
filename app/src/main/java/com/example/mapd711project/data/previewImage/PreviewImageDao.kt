package com.example.mapd711project.data.previewImage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PreviewImageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPreviewImages(previewImages: List<PreviewImage>)

    @Query("SELECT * FROM preview_images WHERE hotelId = :hotelId")
    suspend fun getPreviewImagesByHotelId(hotelId: Int): List<PreviewImage>
    // Add more queries as needed
}
