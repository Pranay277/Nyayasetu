package com.example.evotingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var voterIdInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var verifyVoterButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Force Light Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_main)

        voterIdInput = findViewById(R.id.voterIdInput)
        emailInput = findViewById(R.id.emailInput)
        verifyVoterButton = findViewById(R.id.verifyVoterButton)

        verifyVoterButton.setOnClickListener {
            val voterId = voterIdInput.text.toString().trim()
            val email = emailInput.text.toString().trim()

            if (voterId.isNotEmpty() && email.isNotEmpty()) {
                validateVoter(voterId, email)
            } else {
                Toast.makeText(this, "Please enter both Voter ID and Email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateVoter(voterId: String, email: String) {
        Log.d("CHECK_INPUT", "ID = '$voterId' | EMAIL = '$email'")

        val db = FirebaseFirestore.getInstance()
        val voterRef = db.collection("Voters").document(voterId)

        voterRef.get().addOnSuccessListener { document ->

            if (!document.exists()) {
                Toast.makeText(this, "Invalid Voter ID or Email!", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

            val storedEmail = document.getString("email")
            val hasVoted = document.getBoolean("hasvoted") ?: false

            // ❗ Already voted → Stop here
            if (hasVoted) {
                Toast.makeText(this, "You have already cast your vote!", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }

            // ❗ Email mismatch
            if (storedEmail != email) {
                Toast.makeText(this, "Invalid Voter ID or Email!", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

            // ✅ Valid voter → Send OTP
            sendOtpToEmail(email)

        }.addOnFailureListener {
            Toast.makeText(this, "Error validating voter", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendOtpToEmail(email: String) {
        Log.e("OTP_DEBUG", "Sending OTP to: $email")
        val otp = generateOtp()

        val emailContent = """
        Dear Voter,

        Your OTP for voting is: $otp.

        Please enter this OTP within the app to proceed with your voting.

        Best regards,
        The Voting Team
        """.trimIndent()

        val emailData = SendGridEmailData(
            personalizations = listOf(Personalization(listOf(Recipient(email)))),
            from = From("gpranay988@gmail.com", "The Voting Team"),
            subject = "Your OTP Code for Voting",
            content = listOf(Content("text/plain", emailContent))
        )

        val apiService = RetrofitClientInstance.retrofitInstance.create(SendGridAPIService::class.java)
        apiService.sendEmail(emailData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    navigateToOtpVerificationActivity(otp)
                    Toast.makeText(this@MainActivity, "OTP successfully sent to $email", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("SendOTP", "Failed to send OTP. Response: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@MainActivity, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("SendOTP", "Error sending OTP: ${t.message}")
                Toast.makeText(this@MainActivity, "Error sending OTP", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToOtpVerificationActivity(otp: String) {
        val voterId = voterIdInput.text.toString().trim()
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.putExtra("OTP", otp)
        intent.putExtra("VOTER_ID", voterId)
        startActivity(intent)
    }

    private fun generateOtp(): String {
        return (100000..999999).random().toString()
    }
}
