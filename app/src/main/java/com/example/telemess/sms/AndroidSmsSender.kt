package com.example.telemess.sms

import android.telephony.SmsManager

class AndroidSmsSender : SmsSender {
    override fun send(number: String, text: String) {
        SmsManager.getDefault().sendTextMessage(number, null, text, null, null)
    }
}