package com.example.evotingapp

data class SendGridEmailData(
    val personalizations: List<Personalization>,
    val from: From,
    val subject: String,
    val content: List<Content>
)

data class Personalization(val to: List<Recipient>)
data class Recipient(val email: String)
data class From(val email: String, val name: String)
data class Content(val type: String, val value: String)
