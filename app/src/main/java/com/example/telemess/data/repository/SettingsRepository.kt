package com.example.telemess.data.repository

import android.util.Log
import com.example.telemess.data.db.QuietHoursDAO
import com.example.telemess.data.model.QuietHoursSettings
import com.example.telemess.ui.quiet.QuietHoursUiState
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val quietHoursDao: QuietHoursDAO
) {

    fun observeSettings() = quietHoursDao.observeSettings()

    suspend fun getSettings(): QuietHoursSettings? {
        return quietHoursDao.getSettings()
    }

    suspend fun saveSettings(settings: QuietHoursSettings) {
        quietHoursDao.upsert(settings)
    }

    suspend fun clearSettings() {
        quietHoursDao.clear()
    }

    /**
     * Returns settings or default values if not yet created.
     */
    suspend fun getOrCreateDefault(): QuietHoursSettings {
        val existing = quietHoursDao.getSettings()
        Log.d("DB", "Loaded settings: $existing")

        if (existing != null) return existing
        return getSettings() ?: QuietHoursSettings(
            id = 0,
            isEnabled = true,
            startHour = 22,
            startMinute = 0,
            endHour = 7,
            endMinute = 0,
            isAutoSmsEnabled = false,
            smsTemplate = ""
        ).also {
            saveSettings(it)
        }
    }
}