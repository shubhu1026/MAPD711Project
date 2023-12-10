package com.example.mapd711project.data.customer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val customerId: Int = 0,
    val email: String,
    val password: String,
    var name: String,
    var phone: String
)

