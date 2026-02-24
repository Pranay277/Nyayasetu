🗳️ Android Voting App with Email OTP Verification

____________________________________________________________________________________________________________

📱 Overview

.This is a Secure Android Voting Application developed using Kotlin and Android Studio.

The app uses Email OTP verification via SendGrid API to authenticate users before allowing them to vote.

This project demonstrates:

Android development using Kotlin

API integration using Retrofit

Email authentication using SendGrid

RecyclerView implementation

Secure voting workflow

------------------------------------------------------------------------------------------------------------
✨ Features

📧 Email OTP Verification

🔐 Secure Authentication

👥 Candidate List Display

🗳️ Vote Casting System

✅ Vote Success Confirmation

🌐 SendGrid API Integration

⚡ Fast and Lightweight App

📱 User-Friendly Interface

------------------------------------------------------------------------------------------------------------

🛠️ Tech Stack

Language: Kotlin

IDE: Android Studio

Networking: Retrofit

Email API: SendGrid

UI: XML, RecyclerView

Build Tool: Gradle

------------------------------------------------------------------------------------------------------------
📂 Project Structure
VotingApp/
│
├── app/
│   ├── activities/
│   │   ├── MainActivity.kt
│   │   ├── OtpVerificationActivity.kt
│   │   ├── VotingActivity.kt
│   │   └── VoteSuccessActivity.kt
│   │
│   ├── adapters/
│   │   ├── CandidateAdapter.kt
│   │   └── CandidateListAdapter.kt
│   │
│   ├── models/
│   │   └── Candidate.kt
│   │
│   ├── network/
│   │   ├── RetrofitClientInstance.kt
│   │   ├── SendGridAPIService.kt
│   │   └── SendGridEmailData.kt
│   │
│   └── res/
│
├── build.gradle.kts
└── settings.gradle.kts

------------------------------------------------------------------------------------------------------------
🔄 App Workflow

User opens the app

User enters email

OTP is sent using SendGrid API

User enters OTP

OTP is verified

Candidate list is displayed

User selects candidate

Vote success screen is shown

------------------------------------------------------------------------------------------------------------
Step 1: Clone Repository

git clone https://github.com/yourusername/voting-app.git

Step 2: Open in Android Studio

Open Android Studio

Click Open

Select project folder

Step 3: Add SendGrid API Key

Open:

SendGridAPIService.kt

Add your SendGrid API key.

Step 4: Run the App

Connect Android device OR start emulator

Click Run ▶️

------------------------------------------------------------------------------------------------------------
🔐 Security

OTP-based authentication

Email verification

Secure API communication

------------------------------------------------------------------------------------------------------------
🎯 Learning Outcomes

This project demonstrates:

Android development using Kotlin

REST API integration using Retrofit

Email authentication system

RecyclerView usage

Activity navigation

Secure user verification

------------------------------------------------------------------------------------------------------------
⭐ Contribute

Feel free to fork this repository and improve it.

------------------------------------------------------------------------------------------------------------
📜 License

This project is for educational purposes.
