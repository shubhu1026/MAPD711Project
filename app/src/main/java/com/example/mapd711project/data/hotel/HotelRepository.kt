package com.example.mapd711project.data.hotel

import com.example.mapd711project.data.previewImage.PreviewImage
import com.example.mapd711project.data.previewImage.PreviewImageDao

class HotelRepository(private val hotelDao: HotelDao, private val previewImageDao: PreviewImageDao) {
    suspend fun insertHotel(hotel: Hotel){
        hotelDao.insertHotel(hotel)
    }

    suspend fun insertHotels(pizzas: List<Hotel>){
        hotelDao.insertHotels(pizzas)
    }

    suspend fun getAllHotels(): List<Hotel> {
        return hotelDao.getAllHotels()
    }

    suspend fun insertPreviewImages(previewImages: List<PreviewImage>) {
        previewImageDao.insertPreviewImages(previewImages)
    }

    suspend fun getPreviewImagesByHotelId(hotelId: Int): List<PreviewImage> {
        return previewImageDao.getPreviewImagesByHotelId(hotelId)
    }
}

