package com.example.evotingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class VotingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private lateinit var voterId: String
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Force Light Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_voting)

        recyclerView = findViewById(R.id.rvCandidates)
        recyclerView.layoutManager = LinearLayoutManager(this)

        voterId = intent.getStringExtra("VOTER_ID").orEmpty()

        // 🔥 Block access if user already voted
        checkIfAlreadyVoted()

        setupBiometricPrompt()
    }

    private fun checkIfAlreadyVoted() {
        db.collection("Voters").document(voterId).get()
            .addOnSuccessListener { doc ->
                val hasVoted = doc.getBoolean("hasvoted") ?: false

                if (hasVoted) {
                    Toast.makeText(
                        this,
                        "You have already cast your vote!",
                        Toast.LENGTH_LONG
                    ).show()

                    // Redirect to main screen
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    // Continue only if NOT voted
                    fetchCandidates()
                }
            }
    }

    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(
            this, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    Toast.makeText(this@VotingActivity, "Authentication successful!", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(this@VotingActivity, "Authentication failed!", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(this@VotingActivity, "Error: $errString", Toast.LENGTH_SHORT).show()
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate to cast your vote")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()
    }

    private fun authenticateBiometric(onSuccess: () -> Unit) {
        biometricPrompt.authenticate(promptInfo)

        biometricPrompt = BiometricPrompt(
            this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    Toast.makeText(this@VotingActivity, "Authentication successful!", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(this@VotingActivity, "Authentication failed!", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(this@VotingActivity, "Error: $errString", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun fetchCandidates() {
        if (voterId.isEmpty()) {
            Toast.makeText(this, "Voter ID missing", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("Voters").document(voterId).get()
            .addOnSuccessListener { vDoc ->
                if (!vDoc.exists()) {
                    Toast.makeText(this, "Voter not found!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val constituency = vDoc.getString("constituency") ?: ""
                if (constituency.isEmpty()) {
                    Toast.makeText(this, "Constituency missing!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                db.collection("Candidates")
                    .whereEqualTo("constituency", constituency)
                    .get()
                    .addOnSuccessListener { cDocs ->

                        val list = mutableListOf<Candidate>()

                        for (c in cDocs) {
                            list.add(
                                Candidate(
                                    c.id,
                                    c.getString("name") ?: "",
                                    c.getString("party") ?: "",
                                    c.getString("logoUrl") ?: "",
                                    c.getString("constituency") ?: "",
                                    c.getLong("votes")?.toInt() ?: 0
                                )
                            )
                        }

                        recyclerView.adapter =
                            CandidateListAdapter(this, list) { selected ->
                                authenticateBiometric { castVote(selected) }
                            }
                    }
            }
    }

    private fun castVote(candidate: Candidate) {
        val voterRef = db.collection("Voters").document(voterId)
        val candidateRef = db.collection("Candidates").document(candidate.id)
        val votesCollection = db.collection("Votes")

        db.runTransaction { txn ->

            val oldVote = txn.get(votesCollection.document(voterId))
            if (oldVote.exists()) throw Exception("You have already voted!")

            val cSnap = txn.get(candidateRef)
            val currentVotes = cSnap.getLong("votes") ?: 0

            val vSnap = txn.get(voterRef)

            txn.update(candidateRef, "votes", currentVotes + 1)
            txn.update(voterRef, "hasvoted", true)

            txn.set(
                votesCollection.document(voterId),
                mapOf(
                    "voterId" to voterId,
                    "name" to vSnap.getString("name"),
                    "email" to vSnap.getString("email"),
                    "constituency" to vSnap.getString("constituency"),
                    "votedFor" to candidate.name,
                    "timestamp" to System.currentTimeMillis()
                )
            )

        }.addOnSuccessListener {
            Toast.makeText(this, "Vote Recorded!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, VoteSuccessActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
