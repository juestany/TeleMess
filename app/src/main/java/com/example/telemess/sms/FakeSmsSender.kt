package com.example.telemess.sms

import android.util.Log

class FakeSmsSender : SmsSender {
    override fun send(number: String, text: String) {
        Log.d("FAKE_SMS", "SMS to $number: $text")
    }
}