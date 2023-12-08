package com.example.mapd711project.data.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CustomerViewModelFactory(private val customerRepository: CustomerRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            return CustomerViewModel(customerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}