package com.example.mapd711project.data.previewImage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preview_images")
data class PreviewImage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hotelId: Int,
    val imagePath: String,
)