package com.example.evotingapp
data class Candidate(
    val id: String,
    val name: String,
    val party: String,
    val logoUrl: String, // URL for the candidate's logo
    val constituency: String,
    val votes: Int
)
