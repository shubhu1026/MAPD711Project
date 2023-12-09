package com.example.mapd711project

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mapd711project.databinding.ActivityBookingDetailsBinding
import java.util.Calendar

class BookingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailsBinding

    private var checkInDateSet = false
    private var checkInCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                // If all conditions are met, proceed to the next activity (PaymentActivity)
                startActivity(Intent(this, PaymentActivity::class.java))
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
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
}