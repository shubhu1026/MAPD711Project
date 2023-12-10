package com.example.mapd711project

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mapd711project.databinding.ActivityBookingDetailsBinding
import com.google.android.material.navigation.NavigationView
import java.util.Calendar

class BookingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailsBinding

    private var checkInDateSet = false
    private var checkInCalendar = Calendar.getInstance()

    private lateinit var sharedPreferences: SharedPreferences

    private var hotelId: Int = -1
    private var hotelName: String = "Hotel Details"

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val intent = intent
        hotelId = intent.getIntExtra("hotelId", -1)
        hotelName = intent.getStringExtra("hotelName") ?: "Hotel Details"

        binding.btnCheckInDate.setOnClickListener {
            showDatePicker(true)
        }

        binding.btnCheckOutDate.setOnClickListener {
            if (checkInDateSet) {
                showDatePicker(false)
            } else {
                Toast.makeText(this, "Please select a check-in date first.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.bookButton.setOnClickListener {
            if (!checkInDateSet) {
                Toast.makeText(this, "Please select a check-in date first.", Toast.LENGTH_SHORT).show()
            } else if (binding.btnCheckOutDate.text.isEmpty()) {
                Toast.makeText(this, "Please select a check-out date.", Toast.LENGTH_SHORT).show()
            } else if (!isRoomCountValid()) {
                Toast.makeText(this, "Please enter a valid room count.", Toast.LENGTH_SHORT).show()
            } else {
                val checkInDate = binding.btnCheckInDate.text.toString().trim()
                val checkOutDate = binding.btnCheckOutDate.text.toString().trim()
                val roomCount = binding.roomsInput.text.toString().toIntOrNull()

                val myIntent = Intent(this, BookingSummaryActivity::class.java)
                myIntent.putExtra("hotelId", hotelId)
                myIntent.putExtra("hotelName", hotelName)
                myIntent.putExtra("checkInDate", checkInDate)
                myIntent.putExtra("checkOutDate", checkOutDate)
                myIntent.putExtra("roomCount", roomCount)
                startActivity(myIntent)
            }
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
    private fun isRoomCountValid(): Boolean {
        val roomCount = binding.roomsInput.text.toString().toIntOrNull()
        return roomCount != null && roomCount > 0 // Validate if room count is a positive integer
    }

    private fun showDatePicker(isCheckInDate: Boolean) {
        val calendar = if (isCheckInDate) checkInCalendar else Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            R.style.DatePickerTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                if (isCheckInDate) {
                    checkInDateSet = true
                    checkInCalendar.set(selectedYear, selectedMonth, selectedDay)
                    binding.btnCheckInDate.text = selectedDate
                } else {
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDay)
                    }
                    if (selectedCalendar.after(checkInCalendar)) {
                        binding.btnCheckOutDate.text = selectedDate
                    } else {
                        Toast.makeText(this, "Checkout date cannot be before check-in date.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        if (isCheckInDate) {
            datePickerDialog.show()
        } else {
            datePickerDialog.datePicker.minDate = checkInCalendar.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun setupNavigationView() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu
        navView.menu.clear()

        // Add new custom menu items
        menu.add("Home")
        menu.add("My Profile")
        menu.add("Search Hotels")
        menu.add(hotelName)
        menu.add("Logout")

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.title) {
                "Home" -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                "My Profile" -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                "Search Hotels" -> {
                    startActivity(Intent(this, SearchHotelsActivity::class.java))
                    true
                }
                hotelName -> {
                    val intent = Intent(this@BookingDetailsActivity, HotelDetailsActivity::class.java)
                    intent.putExtra("hotelId", hotelId)
                    intent.putExtra("hotelName", hotelName)
                    startActivity(intent)
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