package com.example.mapd711project.data.customer

class CustomerRepository(private val customerDao: CustomerDao) {
    suspend fun insertCustomer(customer: Customer){
        customerDao.insertCustomer(customer)
    }

    suspend fun insertCustomers(customers: List<Customer>){
        customerDao.insertCustomers(customers)
    }

    suspend fun getAllCustomers(): List<Customer>{
        return customerDao.getAllCustomers()
    }

    suspend fun getCustomerById(customerId: Int): Customer? {
        return customerDao.getCustomerById(customerId)
    }

    suspend fun getCustomerNameById(customerId: Int): String?{
        return customerDao.getFirstNameById(customerId) + customerDao.getLastNameById(customerId)
    }

    suspend fun getCustomerByUsername(username: String): Customer? {
        return customerDao.getCustomerByUsername(username)
    }

    suspend fun getCustomerIdByUsername(username: String): Int? {
        val customer = customerDao.getCustomerByUsername(username)
        return customer?.customerId
    }

    suspend fun updateCustomer(customer: Customer) {
        customerDao.updateCustomer(customer)
    }

    suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(customer)
    }

    suspend fun deleteAllCustomers() {
        customerDao.deleteAllCustomers()
    }
}