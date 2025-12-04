package com.example.evotingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var otpInput: EditText
    private lateinit var verifyOtpButton: Button
    private lateinit var otp: String
    private lateinit var voterId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Force Light Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_otp_verification)

        otpInput = findViewById(R.id.otpInput)
        verifyOtpButton = findViewById(R.id.verifyOtpButton)

        otp = intent.getStringExtra("OTP").toString()
        voterId = intent.getStringExtra("VOTER_ID").toString()

        verifyOtpButton.setOnClickListener {
            val enteredOtp = otpInput.text.toString().trim()
            if (enteredOtp == otp) {
                Toast.makeText(this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, VotingActivity::class.java)
                intent.putExtra("VOTER_ID", voterId)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Invalid OTP, please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
