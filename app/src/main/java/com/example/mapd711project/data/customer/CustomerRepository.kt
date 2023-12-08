package com.example.mapd711project.data.customer

class CustomerRepository(private val customerDao: CustomerDao) {

    // Function to insert a new customer
    suspend fun insertCustomer(customer: Customer) {
        customerDao.insertCustomer(customer)
    }

    suspend fun doesEmailExist(email: String): Boolean {
        return customerDao.doesEmailExist(email) != null
    }

    suspend fun getCustomerByEmailAndPassword(email: String, password: String): Customer? {
        return customerDao.getCustomerByEmailAndPassword(email, password)
    }
}