package com.example.mapd711project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.customer.Customer
import com.example.mapd711project.data.customer.CustomerRepository
import com.example.mapd711project.data.customer.CustomerViewModel
import com.example.mapd711project.data.customer.CustomerViewModelFactory
import com.example.mapd711project.databinding.ActivitySignUpBinding
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var customerViewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customerRepository = CustomerRepository(AppDatabase.getDatabase(applicationContext).customerDao())
        val customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelFactory)[CustomerViewModel::class.java]

        binding.signUpButton.setOnClickListener {
            signUpUser()
        }

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun signUpUser() {
        if (areInputsValid()) {
            val email = binding.emailInput.text.toString()
            // Call function with the entered email
            if(!doesEmailExist(email)){
                val email = binding.emailInput.text.toString()
                val password = binding.passwordInput.text.toString()

                val customer: Customer = Customer(0, email, password)

                saveUserToDatabase(customer)

                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun saveUserToDatabase(customer: Customer) {
        lifecycleScope.launch{
            customerViewModel.insertCustomer(customer)
            Toast.makeText(this@SignUpActivity, "User Registered", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doesEmailExist(email: String): Boolean {
        var emailExists = false
        lifecycleScope.launch {
            emailExists = customerViewModel.doesEmailExist(email)
            if (emailExists) {
                // Email exists in the database, show an error message or handle it
                Toast.makeText(this@SignUpActivity, "Email already exists", Toast.LENGTH_SHORT).show()
            } else {
                // Email doesn't exist, proceed with registration
                // Call your registration function or navigate to the next screen
            }
        }
        return emailExists
    }


    private fun areInputsValid(): Boolean{
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val confirmPassword = binding.confirmPasswordInput.text.toString()

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val isEmailValid = email.matches(emailPattern.toRegex())

        val isPasswordValid = password.length >= 8

        val doPasswordsMatch = password == confirmPassword

        if (!isEmailValid) {
            Toast.makeText(this@SignUpActivity, "Enter valid email", Toast.LENGTH_SHORT).show()
            return false
        } else if (!isPasswordValid) {
            Toast.makeText(this@SignUpActivity, "Enter valid password", Toast.LENGTH_SHORT).show()
            return false
        } else if (!doPasswordsMatch) {
            Toast.makeText(this@SignUpActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }
}