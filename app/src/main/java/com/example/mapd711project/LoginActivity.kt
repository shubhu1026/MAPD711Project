package com.example.mapd711project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.customer.CustomerRepository
import com.example.mapd711project.data.customer.CustomerViewModel
import com.example.mapd711project.data.customer.CustomerViewModelFactory
import com.example.mapd711project.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var customerViewModel: CustomerViewModel

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val customerRepository = CustomerRepository(AppDatabase.getDatabase(applicationContext).customerDao())
        val customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelFactory)[CustomerViewModel::class.java]

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loginUser(){
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        lifecycleScope.launch {
            val emailExists = customerViewModel.doesEmailExist(email)
            if (emailExists) {
                // Email exists, proceed to validate credentials
                val customer = customerViewModel.getCustomerByEmailAndPassword(email, password)
                if (customer != null) {
                    saveUserData(email)
                    startActivity(Intent(this@LoginActivity, SearchHotelsActivity::class.java))
                    //startActivity(Intent(this@LoginActivity, MyBookingsActivity::class.java))
                } else {
                    // Invalid credentials, show an error message
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Email does not exist, show an error message or handle accordingly
                Toast.makeText(this@LoginActivity, "Email does not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }
}