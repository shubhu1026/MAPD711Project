package com.example.mapd711project.data.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapd711project.data.customer.Customer
import kotlinx.coroutines.launch

class AdminViewModel(private val adminRepository: AdminRepository): ViewModel() {

    suspend fun getAdminByEmailAndPassword(email: String, password: String): Admin? {
        return adminRepository.getAdminByEmailAndPassword(email, password)
    }

    fun insertAdmin(admin: Admin) {
        viewModelScope.launch {
            adminRepository.insertAdmin(admin)
        }
    }

    fun updateAdmin(admin: Admin) {
        viewModelScope.launch {
            adminRepository.updateAdmin(admin)
        }
    }

    fun deleteAdmin(admin: Admin) {
        viewModelScope.launch {
            adminRepository.deleteAdmin(admin)
        }
    }
}
