package com.example.mapd711project.data.admin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admins")
data class Admin(
    @PrimaryKey(autoGenerate = true) val employeeId: Int = 0,
    val userName: String,
    val password: String,
    val firstname: String,
    val lastname: String
)