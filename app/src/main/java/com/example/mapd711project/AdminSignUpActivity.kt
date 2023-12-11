package com.example.mapd711project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.admin.Admin
import com.example.mapd711project.data.admin.AdminRepository
import com.example.mapd711project.data.admin.AdminViewModel
import com.example.mapd711project.data.admin.AdminViewModelFactory
import com.example.mapd711project.databinding.ActivityAdminSignUpBinding
import kotlinx.coroutines.launch

class AdminSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminSignUpBinding
    private lateinit var adminViewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adminRepository = AdminRepository(AppDatabase.getDatabase(this).adminDao())
        val adminViewModelFactory = AdminViewModelFactory(adminRepository)
        adminViewModel = ViewModelProvider(this, adminViewModelFactory)[AdminViewModel::class.java]

        binding.addAdminButton.setOnClickListener {
            signUpAdmin()
        }
    }

    private fun signUpAdmin() {
        if (areInputsValid()) {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val name = binding.nameInput.text.toString()

            val admin = Admin(0, email, password, name) // Create an Admin object

            saveAdminToDatabase(admin)
        }
    }

    private fun saveAdminToDatabase(admin: Admin) {
        lifecycleScope.launch {
            adminViewModel.insertAdmin(admin)
            Toast.makeText(this@AdminSignUpActivity, "Admin Registered", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@AdminSignUpActivity, AdminActivity::class.java))
        }
    }

    private fun areInputsValid(): Boolean {
        // Check if email and password are valid
        // Implement your validation logic here
        return true // Return true if inputs are valid, otherwise false
    }
}
