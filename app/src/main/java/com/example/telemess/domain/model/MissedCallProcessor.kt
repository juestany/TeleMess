package com.example.telemess.domain.model

import android.util.Log
import com.example.telemess.data.db.AppDatabase
import com.example.telemess.data.model.CallType
import com.example.telemess.data.model.MissedCallEntity
import com.example.telemess.data.repository.SettingsRepository
import com.example.telemess.sms.SmsSender
import java.util.Calendar

class MissedCallProcessor(
    private val db: AppDatabase,
    private val settingsRepository: SettingsRepository,
    private val smsSender: SmsSender
) {

    suspend fun handleCall(event: IncomingCallEvent) {
        if (event.wasAnswered) return

        val settings = settingsRepository.getOrCreateDefault()

        val now = Calendar.getInstance()
        val currentMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
        val startMinutes = settings.startHour * 60 + settings.startMinute
        val endMinutes = settings.endHour * 60 + settings.endMinute

        val inQuietHours = settings.isEnabled && if (startMinutes <= endMinutes) {
            currentMinutes in startMinutes until endMinutes
        } else {
            currentMinutes >= startMinutes || currentMinutes < endMinutes
        }

        var smsSent = false
        if (inQuietHours && settings.isAutoSmsEnabled) {
            try {
                smsSender.send(event.phoneNumber, settings.smsTemplate)
                smsSent = true
            } catch (e: Exception) {
                Log.e("MissedCallProcessor", "Failed to send SMS: ${e.message}")
            }
        }

        val callType = when {
            event.wasRejected && inQuietHours -> CallType.REJECTED_QUIET_HOURS
            event.wasRejected -> CallType.REJECTED_MANUALLY
            else -> CallType.MISSED
        }

        db.missedCallDao().insertMissedCall(
            MissedCallEntity(
                phoneNumber = event.phoneNumber,
                timestamp = event.timestamp,
                callType = callType,
                smsSent = smsSent,
                displayedToUser = false
            )
        )
    }
}