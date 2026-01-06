package com.example.telemess.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telemess.data.model.CallType
import com.example.telemess.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//private var Any.value: Boolean
//private val HomeScreenViewModel._autoSmsEnabled: Any

class HomeScreenViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _quietHoursEnabled = MutableStateFlow(true)
    val quietHoursEnabled: StateFlow<Boolean> = _quietHoursEnabled

    // --- SMS settings from QuietHours ---
    private val _smsTemplate = MutableStateFlow("")
    val smsTemplate: StateFlow<String> = _smsTemplate

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _sendSmsEnabled = MutableStateFlow(false)
    val autoSmsEnabled: StateFlow<Boolean> = _sendSmsEnabled

    init {
        viewModelScope.launch {
            val settings = settingsRepository.getOrCreateDefault()
            _quietHoursEnabled.value = settings.isEnabled
            _smsTemplate.value = settings.smsTemplate
            _autoSmsEnabled.value = settings.isAutoSmsEnabled
        }
    }

    private val _missedCalls = MutableStateFlow(
        listOf(
            HomeMissedCallUi(
                id = 1,
                phoneNumber = "+48 123 456 789",
                timestamp = System.currentTimeMillis(),
                callType = CallType.REJECTED_QUIET_HOURS
                ,isNew = true
            ),
            HomeMissedCallUi(
                id = 2,
                phoneNumber = "+48 987 654 321",
                timestamp = System.currentTimeMillis() - 3_600_000,
                callType = CallType.MISSED,
                isNew = false
            )
        )
    )

    fun updateSmsTemplate(text: String) {
        _smsTemplate.value = text
        viewModelScope.launch {
            settingsRepository.saveSettings(
                settingsRepository.getOrCreateDefault().copy(
                    smsTemplate = text
                )
            )
        }
    }

    fun toggleAutoSms() {
        _autoSmsEnabled.value = !_autoSmsEnabled.value
        viewModelScope.launch {
            val settings = settingsRepository.getOrCreateDefault()
            settingsRepository.saveSettings(
                settings.copy(isAutoSmsEnabled = _autoSmsEnabled.value)
            )
        }
    }

    fun toggleQuietHours(enabled: Boolean) {
        _quietHoursEnabled.value = enabled
        viewModelScope.launch {
            val settings = settingsRepository.getOrCreateDefault()
            settingsRepository.saveSettings(
                settings.copy(isEnabled = enabled)
            )
        }
    }

    val missedCalls: StateFlow<List<HomeMissedCallUi>> = _missedCalls

    fun markAsRead(callId: Int) {
        _missedCalls.value = _missedCalls.value.map {
            if (it.id == callId) it.copy(isNew = false) else it
        }
    }
}