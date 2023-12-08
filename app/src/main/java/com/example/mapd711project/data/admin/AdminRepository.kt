package com.example.mapd711project.data.admin

class AdminRepository(private val adminDao: AdminDao) {
    suspend fun insertAdmin(admin: Admin){
        adminDao.insertAdmin(admin)
    }

    suspend fun getAllAdmins(): List<Admin>{
        return adminDao.getAllAdmins()
    }

    suspend fun getAdminByUsername(username: String): Admin? {
        return adminDao.getAdminByUsername(username)
    }

    suspend fun updateAdmin(admin: Admin) {
        adminDao.updateAdmin(admin)
    }

    suspend fun deleteAdmin(admin: Admin) {
        adminDao.deleteAdmin(admin)
    }

    suspend fun deleteAllAdmins() {
        adminDao.deleteAllAdmins()
    }
}