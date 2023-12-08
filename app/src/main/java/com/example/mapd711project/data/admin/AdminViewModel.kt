package com.example.mapd711project.data.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AdminViewModel(private val adminRepository: AdminRepository): ViewModel() {
    private val _admins = MutableLiveData<List<Admin>>()

    private val _fetchedAdmin = MutableLiveData<Admin?>()

    val fetchedAdmin: LiveData<Admin?>
        get() = _fetchedAdmin
    val admins: LiveData<List<Admin>>
        get() = _admins

    private fun getAdmins() {
        viewModelScope.launch {
            val result = adminRepository.getAllAdmins()
            _admins.value = result
        }
    }

    fun getAdminByUsername(username: String) {
        viewModelScope.launch {
            _fetchedAdmin.value = adminRepository.getAdminByUsername(username)
        }
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

    fun deleteAllAdmins() {
        viewModelScope.launch {
            adminRepository.deleteAllAdmins()
        }
    }
}
