package com.example.telemess

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.provider.CallLog
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log

class MessagesCallsService : Service() {
    private lateinit var telephonyManager: TelephonyManager
    private var callAnswered = false
    private var lastIncomingNumber: String? = null

    override fun onCreate() {
        super.onCreate()

        lastIncomingNumber = getLastMissedCallNumber(this)

        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        telephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE)

        Log.d("MessagesCallsService", "Service started")
    }

    private val callListener = object : PhoneStateListener() {

        override fun onCallStateChanged(state: Int, incomingNumber: String?) {
            when (state) {

                TelephonyManager.CALL_STATE_RINGING -> {
                    lastIncomingNumber = incomingNumber
                    callAnswered = false
                    Log.d("MessagesCallsService", "Incoming call: $incomingNumber")
                }

                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    // call was answered
                    callAnswered = true
                    Log.d("MessagesCallsService", "Call answered")
                }

                TelephonyManager.CALL_STATE_IDLE -> {
                    // call ended
                    if (!callAnswered) {
                        // call ended and was missed → try reading last missed call
                        val number = getLastMissedCallNumber(this@MessagesCallsService)
                        Log.d("Service", "Missed call number from CallLog: '$number'")
                        if (!number.isNullOrBlank()) {
                            sendSms(number)
                        } else {
                            Log.e("Service", "Cannot send SMS. No valid number.")
                        }
                    }
                }
            }
        }
    }

    private fun sendSms(number: String) {
        try {
            val sms = SmsManager.getDefault()
            sms.sendTextMessage(number, null, "You called", null, null)
            Log.d("MessagesCallsService", "Sent SMS to $number")
        } catch (e: Exception) {
            Log.e("MessagesCallsService", "SMS failed: ${e.message}")
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        telephonyManager.listen(callListener, PhoneStateListener.LISTEN_NONE)
    }

    private fun getLastMissedCallNumber(context: Context): String? {
        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.TYPE),
            "${CallLog.Calls.TYPE} = ?",
            arrayOf(CallLog.Calls.MISSED_TYPE.toString()),
            "${CallLog.Calls.DATE} DESC"
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val number = it.getString(0)
                return number
            }
        }
        return null
    }
}