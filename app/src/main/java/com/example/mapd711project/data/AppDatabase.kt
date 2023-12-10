package com.example.mapd711project.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mapd711project.data.admin.Admin
import com.example.mapd711project.data.admin.AdminDao
import com.example.mapd711project.data.customer.Customer
import com.example.mapd711project.data.customer.CustomerDao
import com.example.mapd711project.data.booking.Booking
import com.example.mapd711project.data.booking.BookingDao
import com.example.mapd711project.data.hotel.Hotel
import com.example.mapd711project.data.hotel.HotelDao
import com.example.mapd711project.data.previewImage.PreviewImage
import com.example.mapd711project.data.previewImage.PreviewImageDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Admin::class, Customer::class, Hotel::class, Booking::class, PreviewImage::class ], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun adminDao(): AdminDao
    abstract fun customerDao() : CustomerDao
    abstract fun hotelDao(): HotelDao
    abstract fun bookingDao(): BookingDao

    abstract fun previewImageDao(): PreviewImageDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_database"
                )
                    .addCallback(RoomCallback(context))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class RoomCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    // Prepopulate Customers
                    val customerDao = database.customerDao()
                    val customers = listOf(
                        Customer(0, "user1", "pass1", "user 1", "1111111111"),
                        Customer(0, "user2", "pass2", "user 2", "1111111111")
                    )
                    customerDao.insertCustomers(customers)

                    // Prepopulate Pizzas
                    val hotelDao = database.hotelDao()
                    val previewImageDao = database.previewImageDao()
                    val hotels = listOf(
                        Hotel(
                            1,
                            hotelName = "Majestic Plaza Hotel",
                            category = "Popular",
                            price = 180.0,
                            location = "London",
                            address = "12 Park Lane, London",
                            rating = "4.7",
                            hotelDescription = "Elegant hotel near Hyde Park",
                            displayImage = "file:///android_asset/display_images/london_display.jpg"
                        ),
                        Hotel(
                            2,
                            hotelName = "Golden Bay Resort",
                            category = "Popular",
                            price = 220.0,
                            location = "Sydney",
                            address = "34 Bondi Road, Sydney",
                            rating = "4.9",
                            hotelDescription = "Luxury resort overlooking Bondi Beach",
                            displayImage = "file:///android_asset/display_images/sydney_display.jpg"
                        ),
                        Hotel(
                            3,
                            hotelName = "Mountain View Lodge",
                            category = "Popular",
                            price = 150.0,
                            location = "Aspen",
                            address = "78 Snowy Peaks Rd, Aspen",
                            rating = "4.6",
                            hotelDescription = "Cosy lodge in the heart of the Rockies",
                            displayImage = "file:///android_asset/display_images/aspen_display.jpg"
                        ),
                        Hotel(
                            4,
                            hotelName = "Oceanfront Paradise",
                            category = "Popular",
                            price = 320.0,
                            location = "Miami",
                            address = "987 Ocean Drive, Miami",
                            rating = "4.8",
                            hotelDescription = "Luxurious beachfront property",
                            displayImage = "file:///android_asset/display_images/miami_display.jpg"
                        ),
                        Hotel(
                            5,
                            hotelName = "Riverside Retreat",
                            category = "Popular",
                            price = 200.0,
                            location = "Paris",
                            address = "23 Rue de la Seine, Paris",
                            rating = "4.5",
                            hotelDescription = "Quaint hotel by the Seine River",
                            displayImage = "file:///android_asset/display_images/paris_display.jpg"
                        ),
                        Hotel(
                            6,
                            hotelName = "Highland Haven",
                            category = "Recommended",
                            price = 180.0,
                            location = "Edinburgh",
                            address = "56 Royal Mile, Edinburgh",
                            rating = "4.6",
                            hotelDescription = "Charming inn in the historic city center",
                            displayImage = "file:///android_asset/display_images/edinburgh_display.jpg"
                        ),
                        Hotel(
                            7,
                            hotelName = "Tropical Oasis Resort",
                            category = "Recommended",
                            price = 280.0,
                            location = "Bali",
                            address = "123 Beachfront Ave, Bali",
                            rating = "4.9",
                            hotelDescription = "Spectacular resort in a tropical paradise",
                            displayImage = "file:///android_asset/display_images/bali_display.jpg"
                        ),
                        Hotel(
                            8,
                            hotelName = "Alpine Peaks Hotel",
                            category = "Recommended",
                            price = 190.0,
                            location = "Zurich",
                            address = "34 Alpine Way, Zurich",
                            rating = "4.7",
                            hotelDescription = "A mountainous escape in Switzerland",
                            displayImage = "file:///android_asset/display_images/zurich_display.jpg"
                        ),
                        Hotel(
                            9,
                            hotelName = "City Lights Inn",
                            category = "Recommended",
                            price = 210.0,
                            location = "New York",
                            address = "567 Broadway, New York",
                            rating = "4.8",
                            hotelDescription = "Urban retreat in the heart of NYC",
                            displayImage = "file:///android_asset/display_images/newyork_display.jpg"
                        ),
                        Hotel(
                            10,
                            hotelName = "Sunset Serenity Lodge",
                            category = "Recommended",
                            price = 170.0,
                            location = "Maldives",
                            address = "78 Serenity Road, Maldives",
                            rating = "4.9",
                            hotelDescription = "Tranquil getaway on a private island",
                            displayImage = "file:///android_asset/display_images/maldives_display.jpg"
                        )
                    )

                    hotelDao.insertHotels(hotels)

                    val hotelIdsList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

                    val previewImages = mutableListOf<PreviewImage>()

                    hotelIdsList.forEach { hotelId ->
                        val previewImagePaths = listOf(
                            "file:///android_asset/maldives/maldives_1.jpg",
                            "file:///android_asset/maldives/maldives_2.jpg",
                            "file:///android_asset/maldives/maldives_3.jpg",
                            "file:///android_asset/maldives/maldives_4.jpg",
                            "file:///android_asset/maldives/maldives_5.jpg",
                            "file:///android_asset/maldives/maldives_6.jpg",
                            "file:///android_asset/maldives/maldives_7.jpg",
                        )

                        previewImagePaths.forEach { imagePath ->
                            previewImages.add(PreviewImage(0,hotelId, imagePath))
                        }
                    }

                    previewImageDao.insertPreviewImages(previewImages)

                    // Prepopulate Admin
                    val adminDao = database.adminDao()
                    val admin = Admin(0, "admin", "pass", "Admin","1")
                    adminDao.insertAdmin(admin)
                }
            }
        }
    }
}