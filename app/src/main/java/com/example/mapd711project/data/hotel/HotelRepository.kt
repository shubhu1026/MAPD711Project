package com.example.mapd711project.data.hotel

class HotelRepository(private val hotelDao: HotelDao) {
    suspend fun insertHotel(hotel: Hotel){
        hotelDao.insertHotel(hotel)
    }

    suspend fun insertHotels(pizzas: List<Hotel>){
        hotelDao.insertHotels(pizzas)
    }

    suspend fun getAllHotels(): List<Hotel> {
        return hotelDao.getAllHotels()
    }

    suspend fun getHotelByName(hotelName: String): Hotel?{
        return hotelDao.getHotelByName(hotelName)
    }

    suspend fun getHotelIdFromHotelName(hotelName: String): Int? {
        val hotel = hotelDao.getHotelByName(hotelName)
        return hotel?.productId
    }

    suspend fun updateHotel(hotel: Hotel) {
        hotelDao.updateHotel(hotel)
    }

    suspend fun deleteHotel(hotel: Hotel) {
        hotelDao.deleteHotel(hotel)
    }

    suspend fun deleteAllHotels() {
        hotelDao.deleteAllHotels()
    }
}

