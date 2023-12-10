package com.example.mapd711project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.customer.Customer
import com.example.mapd711project.data.customer.CustomerRepository
import com.example.mapd711project.data.customer.CustomerViewModel
import com.example.mapd711project.data.customer.CustomerViewModelFactory
import com.example.mapd711project.databinding.ActivitySignUpBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var customerViewModel: CustomerViewModel

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

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

        drawerLayout = binding.myDrawerLayout
        val navView: NavigationView = findViewById(R.id.nav_view)
        setupNavigationView()
        setupDrawer()

        binding.menuButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        drawerLayout = binding.myDrawerLayout
        setupNavigationView()
        setupDrawer()

        binding.menuButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun signUpUser() {
        if (areInputsValid()) {
            val email = binding.emailInput.text.toString()
            // Call function with the entered email
            if(!doesEmailExist(email)){
                val email = binding.emailInput.text.toString()
                val password = binding.passwordInput.text.toString()

                val customer: Customer = Customer(0, email, password, "", "")

                saveUserToDatabase(customer)
                saveUserData(email)

                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("fromSignUp", true)
                startActivity(intent)
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

    private fun setupNavigationView() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.menu.clear()
        navView.menu.add(0, R.id.nav_item1, 0, "Home")
        navView.menu.add(0, R.id.nav_item2, 0, "Login")

        navView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.nav_item1 -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_item2 -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun saveUserData(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
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