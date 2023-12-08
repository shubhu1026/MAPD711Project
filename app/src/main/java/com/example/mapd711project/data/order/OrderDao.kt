package com.example.mapd711project.data.order

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: Order)

    //read users
    @Query("SELECT * FROM orders")
    fun getAllOrders(): List<Order>

    @Query("SELECT * FROM orders WHERE status = 'Pending'")
    fun getPendingOrders(): LiveData<List<Order>>

    @Query("SELECT * FROM orders WHERE status = 'Completed'")
    fun getCompletedOrders(): LiveData<List<Order>>

    @Update
    suspend fun updateOrderStatus(order: Order)

    //update user
    @Update
    suspend fun updateOrder(order: Order)

    //delete user
    @Delete
    suspend fun deleteOrder(order: Order)

    //deleteAll
    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
}