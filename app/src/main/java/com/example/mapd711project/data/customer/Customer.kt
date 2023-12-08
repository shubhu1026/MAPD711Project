package com.example.mapd711project.data.customer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val customerId: Int = 0,
    val userName: String,
    val password: String,
    var firstname: String,
    var lastname: String,
    var address: String,
    var city: String,
    var postalCode: String
)

