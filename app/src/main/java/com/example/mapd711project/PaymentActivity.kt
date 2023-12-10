package com.example.mapd711project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mapd711project.data.AppDatabase
import com.example.mapd711project.data.booking.Booking
import com.example.mapd711project.data.booking.BookingRepository
import com.example.mapd711project.data.booking.BookingViewModel
import com.example.mapd711project.data.booking.BookingViewModelFactory
import com.example.mapd711project.data.customer.Customer
import com.example.mapd711project.data.customer.CustomerRepository
import com.example.mapd711project.data.customer.CustomerViewModel
import com.example.mapd711project.data.customer.CustomerViewModelFactory
import com.example.mapd711project.databinding.ActivityPaymentBinding
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var bookingViewModel: BookingViewModel

    private var customerId: Int = -1
    private var hotelId: Int = -1
    private var roomCount: Int = -1
    private var checkInDate: String = ""
    private var checkOutDate: String = ""
    private var totalCost: Double = 0.0
    private var bookedBy: String = ""
    private var phone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customerRepository = CustomerRepository(AppDatabase.getDatabase(applicationContext).customerDao())
        val customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelFactory)[CustomerViewModel::class.java]

        val bookingRepository = BookingRepository(AppDatabase.getDatabase(applicationContext).bookingDao())
        val bookingViewModelFactory = BookingViewModelFactory(bookingRepository)
        bookingViewModel = ViewModelProvider(this, bookingViewModelFactory)[BookingViewModel::class.java]

        hotelId = intent.getIntExtra("hotelId", -1)
        checkInDate = intent.getStringExtra("checkInDate") ?: ""
        checkOutDate = intent.getStringExtra("checkOutDate") ?: ""
        bookedBy = intent.getStringExtra("bookedBy") ?: ""
        phone = intent.getStringExtra("phone") ?: ""
        roomCount = intent.getIntExtra("roomCount", 0)
        totalCost = intent.getDoubleExtra("totalCost", 0.0)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        binding.payButton.setOnClickListener {
            if (validateInputs()) {
                if (isUserLoggedIn()) {
                    val userEmail = getUserEmail()
                    getUserIDFromDatabase(userEmail)
                } else {
                    showToast("User is not logged in")
                }
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }
    private fun getUserEmail(): String {
        return sharedPreferences.getString("user_email", "") ?: ""
    }

    private fun getUserIDFromDatabase(email: String) {
        val userDao = AppDatabase.getDatabase(applicationContext).customerDao()


        lifecycleScope.launch {
            val customer = customerViewModel.getCustomerByEmail(email)
            if (customer != null) {
                customerId = customer.customerId
                saveBooking()
                startActivity(Intent(this@PaymentActivity, ThanksActivity::class.java))
            }else {
                showToast("Something went Wrong")
            }
        }
    }

    private fun saveBooking() {
        val booking = Booking(0, customerId, bookedBy, phone, hotelId, checkInDate, checkOutDate, roomCount, totalCost)

        lifecycleScope.launch {
            bookingViewModel.insertBooking(booking)
            Toast.makeText(this@PaymentActivity, "Booking Confirmed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        val cardNumber = binding.cardNumberInput.text.toString()
        val expiryDate = binding.cardExpiryInput.text.toString()
        val cvv = binding.cvvInput.text.toString()
        val nameOnCard = binding.nameOnCardInput.text.toString()

        if (cardNumber.isBlank() || cardNumber.length < 16) {
            showToast("Enter a valid card number")
            return false
        }

        if (expiryDate.isBlank() || expiryDate.length < 4) {
            showToast("Enter a valid expiry date")
            return false
        }

        if (cvv.isBlank() || cvv.length < 3) {
            showToast("Enter a valid CVV")
            return false
        }

        if (nameOnCard.isBlank()) {
            showToast("Enter name on the card")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}