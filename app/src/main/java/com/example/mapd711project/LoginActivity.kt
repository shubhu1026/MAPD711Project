package com.example.mapd711project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.admin.AdminRepository
import com.example.mapd711project.data.admin.AdminViewModel
import com.example.mapd711project.data.admin.AdminViewModelFactory
import com.example.mapd711project.data.customer.CustomerRepository
import com.example.mapd711project.data.customer.CustomerViewModel
import com.example.mapd711project.data.customer.CustomerViewModelFactory
import com.example.mapd711project.databinding.ActivityLoginBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var adminViewModel: AdminViewModel

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val customerRepository = CustomerRepository(AppDatabase.getDatabase(applicationContext).customerDao())
        val customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelFactory)[CustomerViewModel::class.java]

        val adminRepository = AdminRepository(AppDatabase.getDatabase(applicationContext).adminDao())
        val adminViewModelFactory = AdminViewModelFactory(adminRepository)
        adminViewModel = ViewModelProvider(this, adminViewModelFactory)[AdminViewModel::class.java]

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
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

    private fun loginUser() {
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (email.isEmpty()) {
            // Check if email is empty or not in a valid format
            Toast.makeText(this@LoginActivity, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty() ) {
            // Check if password is empty or shorter than required length
            Toast.makeText(this@LoginActivity, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val admin = adminViewModel.getAdminByEmailAndPassword(email, password)
            if (admin != null) {
                saveUserData(email)
                startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
            }
            else {
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
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT)
                            .show()
                    } } else {
                // Email does not exist, show an error message or handle accordingly
                Toast.makeText(this@LoginActivity, "Email does not exist", Toast.LENGTH_SHORT)
                    .show()
                }
            }
        }
    }

    private fun setupNavigationView() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu
        navView.menu.clear()

        // Add new custom menu items
        menu.add("Home")
        menu.add("Sign Up")

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.title) {
                "Home" -> {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    true
                }
                "Sign Up" -> {
                    startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
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
}