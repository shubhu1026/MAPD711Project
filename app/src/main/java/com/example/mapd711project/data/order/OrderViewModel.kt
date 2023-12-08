package com.example.mapd711project.data.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OrderViewModel(private val orderRepository: OrderRepository): ViewModel() {
    private val _orders = MutableLiveData<List<Order>>()

    val orders: LiveData<List<Order>> = orderRepository.getPendingOrders()

    private val _updatedOrder = MutableLiveData<Order>()
    val updatedOrder: LiveData<Order> = _updatedOrder

    private fun getOrders() {
        viewModelScope.launch {
            val result = orderRepository.getAllOrders()
            _orders.value = result
        }
    }

    fun getPendingOrders(): LiveData<List<Order>> {
        return orderRepository.getPendingOrders()
    }

    fun getCompletedOrders(): LiveData<List<Order>> {
        return orderRepository.getCompletedOrders()
    }

    fun insertOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.insertOrder(order)
        }
    }

    fun updateOrderStatus(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(order)
            _updatedOrder.value = order
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(order)
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.deleteOrder(order)
        }
    }

    fun deleteAllOrders() {
        viewModelScope.launch {
            orderRepository.deleteAllOrders()
        }
    }
}
