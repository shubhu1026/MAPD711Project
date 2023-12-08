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
import com.example.mapd711project.data.order.Order
import com.example.mapd711project.data.order.OrderDao
import com.example.mapd711project.data.hotel.Hotel
import com.example.mapd711project.data.hotel.HotelDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Admin::class, Customer::class, Hotel::class, Order::class ], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun adminDao(): AdminDao
    abstract fun customerDao() : CustomerDao
    abstract fun pizzaDao(): HotelDao
    abstract fun orderDao(): OrderDao

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
                        Customer(0, "user1", "pass1", "User", "1","default","default","default"),
                        Customer(0, "user2", "pass2", "User", "2","default","default","default")
                        // Add more initial customers as needed
                    )
                    customerDao.insertCustomers(customers)

                    // Prepopulate Pizzas
                    val pizzaDao = database.pizzaDao()
                    val pizzas = listOf(
                        Hotel(0, "Margherita", 12.99, "Veg") ,
                        Hotel(0, "Pepperoni", 14.99, "Non-Veg"),
                        Hotel(0, "Supreme", 16.99, "Non-Veg"),
                        Hotel(0, "Vegetarian", 13.99, "Veg"),
                        Hotel(0, "Hawaiian", 15.99, "Non-Veg"),
                        Hotel(0, "Mushroom Lovers", 14.49, "Veg")
                    )
                    pizzaDao.insertHotels(pizzas)

                    // Prepopulate Admin
                    val adminDao = database.adminDao()
                    val admin = Admin(0, "admin", "pass", "Admin","1")
                    adminDao.insertAdmin(admin)
                }
            }
        }
    }
}