package com.example.mapd711project.data.admin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admins")
data class Admin(
    @PrimaryKey(autoGenerate = true)
    val adminId: Int = 0,
    val email: String,
    val password: String,
    val name: String,
)