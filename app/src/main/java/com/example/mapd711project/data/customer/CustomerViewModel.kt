package com.example.mapd711project.data.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CustomerViewModel(private val customerRepository: CustomerRepository): ViewModel() {

    suspend fun doesEmailExist(email: String): Boolean {
        return customerRepository.doesEmailExist(email)
    }

    suspend fun insertCustomer(customer: Customer){
        customerRepository.insertCustomer(customer)
    }

    suspend fun getCustomerByEmailAndPassword(email: String, password: String): Customer? {
        return customerRepository.getCustomerByEmailAndPassword(email, password)
    }

    suspend fun getCustomerByEmail(email: String): Customer? {
        return customerRepository.getCustomerByEmailAndPassword(email)
    }

}
