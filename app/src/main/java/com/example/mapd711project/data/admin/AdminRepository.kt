package com.example.mapd711project.data.admin

import com.example.mapd711project.data.customer.Customer

class AdminRepository(private val adminDao: AdminDao) {
    suspend fun insertAdmin(admin: Admin){
        adminDao.insertAdmin(admin)
    }

    suspend fun getAdminByEmailAndPassword(email: String, password: String): Admin? {
        return adminDao.getAdminByEmailAndPassword(email, password)
    }

    suspend fun updateAdmin(admin: Admin) {
        adminDao.updateAdmin(admin)
    }

    suspend fun deleteAdmin(admin: Admin) {
        adminDao.deleteAdmin(admin)
    }

}