package com.example.telemess.domain.model

data class IncomingCallEvent(
    val phoneNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val wasAnswered: Boolean,
    val wasRejected: Boolean = false
)