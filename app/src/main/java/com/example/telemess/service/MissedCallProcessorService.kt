package com.example.telemess.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.CallLog
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.telemess.R
import com.example.telemess.data.db.AppDatabase
import com.example.telemess.data.model.CallType
import com.example.telemess.data.model.MissedCallEntity
import com.example.telemess.data.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MissedCallProcessorService : Service() {

    private lateinit var db: AppDatabase
    private lateinit var settingsRepository: SettingsRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        const val CHANNEL_ID = "missed_calls_channel"
        const val NOTIFICATION_ID = 1
        const val POLL_INTERVAL_MS = 3_000L
    }

    override fun onCreate() {
        super.onCreate()

        Log.e("TELEMESS", "MissedCallProcessorService STARTED")

        db = AppDatabase.getInstance(applicationContext)
        settingsRepository = SettingsRepository(db.quietHoursDao())

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

        startPolling()
    }

    // ---------------- POLLING ----------------

    private fun startPolling() {
        serviceScope.launch {
            while (true) {
                try {
                    pollCallLog()
                } catch (e: Exception) {
                    Log.e("TELEMESS", "Polling error: ${e.message}")
                }
                delay(POLL_INTERVAL_MS)
            }
        }
    }

    private suspend fun pollCallLog() {
        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE
            ),
            "${CallLog.Calls.TYPE} = ?",
            arrayOf(CallLog.Calls.MISSED_TYPE.toString()),
            "${CallLog.Calls.DATE} DESC"
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val number = it.getString(0)
                val timestamp = it.getLong(1)

                val alreadyExists =
                    db.missedCallDao().getByTimestamp(timestamp) != null

                if (!alreadyExists) {
                    Log.d("TELEMESS", "New missed call: $number")
                    processMissedCall(number, timestamp)
                }
            }
        }
    }

    // ---------------- BUSINESS LOGIC ----------------

    private fun processMissedCall(number: String, timestamp: Long) {
        serviceScope.launch {
            val settings = settingsRepository.getOrCreateDefault()

            val now = Calendar.getInstance()
            val currentMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
            val startMinutes = settings.startHour * 60 + settings.startMinute
            val endMinutes = settings.endHour * 60 + settings.endMinute

            val inQuietHours = if (startMinutes <= endMinutes) {
                currentMinutes in startMinutes until endMinutes
            } else {
                currentMinutes >= startMinutes || currentMinutes < endMinutes
            }

            val callType =
                if (inQuietHours) CallType.REJECTED_QUIET_HOURS else CallType.MISSED

            var smsSent = false

            if (inQuietHours && settings.isAutoSmsEnabled) {
                try {
                    SmsManager.getDefault().sendTextMessage(
                        number,
                        null,
                        settings.smsTemplate,
                        null,
                        null
                    )
                    smsSent = true
                    Log.d("TELEMESS", "SMS sent to $number")
                } catch (e: Exception) {
                    Log.e("TELEMESS", "SMS failed: ${e.message}")
                }
            }

            db.missedCallDao().insertMissedCall(
                MissedCallEntity(
                    phoneNumber = number,
                    timestamp = timestamp,
                    callType = callType,
                    smsSent = smsSent,
                    displayedToUser = false
                )
            )
        }
    }

    // ---------------- NOTIFICATION ----------------

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("TeleMess active")
            .setContentText("Monitoring missed calls")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "TeleMess",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}