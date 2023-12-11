package com.example.mapd711project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mapd711project.databinding.ActivityAdminBinding
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        binding.activeBookingsButton.setOnClickListener {
            startActivity(Intent(this, AdminActiveBookingsActivity::class.java))
        }

        binding.bookingRequestsButton.setOnClickListener {
            startActivity(Intent(this, AdminBookingRequestsActivity::class.java))
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

    private fun setupNavigationView() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu
        navView.menu.clear()

        // Add new custom menu items
        menu.add("Home")
        menu.add("Add New Admin")
        menu.add("Logout")

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.title) {
                "Home" -> {
                    logoutUser()
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                "Add New Admin" -> {
                    startActivity(Intent(this, AdminSignUpActivity::class.java))
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

    private fun logoutUser() {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", "")
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }

}