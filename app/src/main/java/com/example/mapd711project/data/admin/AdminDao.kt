package com.example.mapd711project.data.admin

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mapd711project.data.customer.Customer

@Dao
interface AdminDao {
    @Insert
    suspend fun insertAdmin(admin: Admin)

    //update user
    @Update
    suspend fun updateAdmin(admin: Admin)

    //delete user
    @Delete
    suspend fun deleteAdmin(admin: Admin)

    @Query("SELECT * FROM admins WHERE email = :email AND password = :password")
    suspend fun getAdminByEmailAndPassword(email: String, password: String): Admin?
}