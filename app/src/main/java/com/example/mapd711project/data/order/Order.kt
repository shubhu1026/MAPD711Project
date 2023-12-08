package com.example.mapd711project.data.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val customerId: Int,
    val customerName: String,
    val pizzaId: Int,
    val pizzaName: String,
    val orderDate: String,
    val quantity: Int,
    val status: String,
    val totalCost: String,
    val pizzaSize: String,
    val pizzaToppings: String,
    val deliveryAddress: String,
)
