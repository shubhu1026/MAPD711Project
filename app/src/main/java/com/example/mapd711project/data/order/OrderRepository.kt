package com.example.mapd711project.data.order

import androidx.lifecycle.LiveData

class OrderRepository(private val orderDao: OrderDao) {
    suspend fun insertOrder(order: Order){
        orderDao.insertOrder(order)
    }

    suspend fun getAllOrders(): List<Order>{
        return orderDao.getAllOrders()
    }

    fun getPendingOrders(): LiveData<List<Order>> {
        return orderDao.getPendingOrders()
    }

    fun getCompletedOrders(): LiveData<List<Order>> {
        return orderDao.getCompletedOrders()
    }

    suspend fun updateOrderStatus(order: Order) {
        orderDao.updateOrderStatus(order)
    }

    suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order)
    }

    suspend fun deleteOrder(order: Order) {
        orderDao.deleteOrder(order)
    }

    suspend fun deleteAllOrders() {
        orderDao.deleteAllOrders()
    }
}