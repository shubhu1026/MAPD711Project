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
import com.example.mapd711project.data.customer.CustomerRepository
import com.example.mapd711project.data.customer.CustomerViewModel
import com.example.mapd711project.data.customer.CustomerViewModelFactory
import com.example.mapd711project.databinding.ActivityProfileBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var customerViewModel: CustomerViewModel

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = applicationContext.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val customerRepository = CustomerRepository(AppDatabase.getDatabase(applicationContext).customerDao())
        val customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelFactory)[CustomerViewModel::class.java]

        setUpProfile()

        binding.saveButton.setOnClickListener {
            updateUserInfo()
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

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun updateUserInfo() {
        val name = binding.nameInput.text.toString()
        val phone = binding.phoneInput.text.toString()

        if (areInputsValid(name, phone)) {
            val email = sharedPreferences.getString("user_email", "").toString()

            lifecycleScope.launch {
                delay(1000)
                val customer = customerViewModel.getCustomerByEmail(email)
                if (customer != null) {
                    // Update user info
                    customer.name = name
                    customer.phone = phone
                    // Call function to update the customer info
                    customerViewModel.updateCustomer(customer)

                    Toast.makeText(this@ProfileActivity, "User Data Updated", Toast.LENGTH_SHORT).show()

                    if (intent.getBooleanExtra("fromSignUp", false)) {
                        // Redirect to LoginActivity if coming from SignUpActivity
                        startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                    } else {
                        // Redirect to the previous activity otherwise
                        onBackPressed()
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Something went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this@ProfileActivity, "Invalid name or phone", Toast.LENGTH_SHORT).show()
        }
    }


    private fun areInputsValid(name: String, phone: String): Boolean {
        val isNameValid = name.isNotEmpty() // Validate name: Not empty
        val isPhoneValid = phone.isNotEmpty() && phone.length >= 10 // Validate phone: Not empty and at least 10 characters

        return isNameValid && isPhoneValid
    }

    private fun setUpProfile() {
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("is_logged_in", false)
        val email: String = sharedPreferences.getString("user_email", "").toString()

        if(!isLoggedIn)
        {
            return
        }

        lifecycleScope.launch {
            val customer = customerViewModel.getCustomerByEmail(email)
            if (customer != null) {
                binding.emailInput.setText(customer.email)

                // Prevent user from editing
                binding.emailInput.isEnabled = false
                binding.emailInput.isFocusable = false
                binding.emailInput.isFocusableInTouchMode = false

                binding.nameInput.setText(customer.name)
                binding.phoneInput.setText(customer.phone)
            }else {
                Toast.makeText(this@ProfileActivity,"Something went Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNavigationView() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu
        navView.menu.clear()

        // Add new custom menu items
        menu.add("Home")
        menu.add("Logout")

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.title) {
                "Home" -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                "Logout" -> {
                    logoutUser()
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun logoutUser() {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", "")
        editor.putBoolean("is_logged_in", true)
        editor.apply()
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

}