package com.example.telemess.sms

interface SmsSender {
    fun send(number: String, text: String)
}