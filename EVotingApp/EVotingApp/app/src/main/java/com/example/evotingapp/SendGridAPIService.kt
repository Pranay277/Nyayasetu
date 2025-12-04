package com.example.evotingapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SendGridAPIService {

    @Headers(
        "Authorization: Bearer ${BuildConfig.SENDGRID_API_KEY}",
        "Content-Type: application/json"
    )
    @POST("mail/send")
    fun sendEmail(@Body emailData: SendGridEmailData): Call<Void>
}
