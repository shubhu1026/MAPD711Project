package com.example.mapd711project.data.admin

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AdminDao {
    @Insert
    suspend fun insertAdmin(admin: Admin)

    //read users
    @Query("SELECT * FROM admins")
    fun getAllAdmins(): List<Admin>

    @Query("SELECT * FROM admins WHERE userName = :username")
    suspend fun getAdminByUsername(username: String): Admin?

    //update user
    @Update
    suspend fun updateAdmin(admin: Admin)

    //delete user
    @Delete
    suspend fun deleteAdmin(admin: Admin)

    //deleteAll
    @Query("DELETE FROM admins")
    suspend fun deleteAllAdmins()
}