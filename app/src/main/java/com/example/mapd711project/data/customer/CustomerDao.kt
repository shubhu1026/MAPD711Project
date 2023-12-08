package com.example.mapd711project.data.customer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CustomerDao {
    @Insert
    suspend fun insertCustomer(customer: Customer)

    @Insert
    suspend fun insertCustomers(customers: List<Customer>)

    //read users
    @Query("SELECT * FROM customers")
    fun getAllCustomers(): List<Customer>

    @Query("SELECT * FROM customers WHERE customerId = :id")
    suspend fun getCustomerById(id: Int): Customer?

    @Query("SELECT * FROM customers WHERE userName = :username")
    suspend fun getCustomerByUsername(username: String): Customer?

    @Query("SELECT firstname FROM customers WHERE customerId = :id")
    suspend fun getFirstNameById(id: Int): String?

    @Query("SELECT lastname FROM customers WHERE customerId = :id")
    suspend fun getLastNameById(id: Int): String?

    //update user
    @Update
    suspend fun updateCustomer(customer: Customer)

    //delete user
    @Delete
    suspend fun deleteCustomer(customer: Customer)

    //deleteAll
    @Query("DELETE FROM customers")
    suspend fun deleteAllCustomers()
}