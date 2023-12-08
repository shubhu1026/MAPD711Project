package com.example.mapd711project.data.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CustomerViewModel(private val customerRepository: CustomerRepository): ViewModel() {
    private val _customers = MutableLiveData<List<Customer>>()

    private val _fetchedCustomer = MutableLiveData<Customer?>()
    val fetchedCustomer: LiveData<Customer?>
        get() = _fetchedCustomer

    val customers: LiveData<List<Customer>>
        get() = _customers

    private fun getCustomers() {
        viewModelScope.launch {
            val result = customerRepository.getAllCustomers()
            _customers.value = result
        }
    }

    suspend fun getCustomerById(customerId: Int): Customer? {
        return customerRepository.getCustomerById(customerId)
    }

    suspend fun getCustomerNameById(customerId: Int): String?{
        return customerRepository.getCustomerNameById(customerId)
    }

    suspend fun getCustomerIdByUsername(username: String): Int? {
            return customerRepository.getCustomerIdByUsername(username)
    }

    fun insertCustomer(customer: Customer) {
        viewModelScope.launch {
            customerRepository.insertCustomer(customer)
        }
    }

    fun getCustomerByUsername(username: String) {
        viewModelScope.launch {
            _fetchedCustomer.value = customerRepository.getCustomerByUsername(username)
        }
    }

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            customerRepository.updateCustomer(customer)
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            customerRepository.deleteCustomer(customer)
        }
    }

    fun deleteAllCustomers() {
        viewModelScope.launch {
            customerRepository.deleteAllCustomers()
        }
    }
}
