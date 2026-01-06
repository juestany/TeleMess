package com.example.telemess.ui.quiet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telemess.data.model.QuietHoursSettings
import com.example.telemess.ui.quiet.QuietHoursUiState
import com.example.telemess.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuietHoursViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuietHoursUiState())
    val uiState: StateFlow<QuietHoursUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = settingsRepository.getOrCreateDefault()
            _uiState.value = settings.toUiState()
        }
    }

    // --- UI actions ---

    fun toggleEnabled(enabled: Boolean) = updateState { copy(enabled = enabled) }

    fun updateStartTime(hour: Int, minute: Int) = updateState { copy(startHour = hour, startMinute = minute) }

    fun updateEndTime(hour: Int, minute: Int) = updateState { copy(endHour = hour, endMinute = minute) }

    fun toggleAutoSms(enabled: Boolean) = updateState { copy(autoSmsEnabled = enabled) }

    fun updateSmsTemplate(text: String) = updateState { copy(smsTemplate = text) }

    fun saveSettings() {
        viewModelScope.launch {
            settingsRepository.saveSettings(_uiState.value.toEntity())
        }
    }

    // --- Helpers ---

    private fun updateState(reducer: QuietHoursUiState.() -> QuietHoursUiState) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    // --- Mappers ---

    private fun QuietHoursSettings.toUiState(): QuietHoursUiState = QuietHoursUiState(
        enabled = isEnabled,
        startHour = startHour,
        startMinute = startMinute,
        endHour = endHour,
        endMinute = endMinute,
        autoSmsEnabled = isAutoSmsEnabled,
        smsTemplate = smsTemplate
    )

    private fun QuietHoursUiState.toEntity(): QuietHoursSettings = QuietHoursSettings(
        id = 0,
        isEnabled = enabled,
        startHour = startHour,
        startMinute = startMinute,
        endHour = endHour,
        endMinute = endMinute,
        isAutoSmsEnabled = autoSmsEnabled,
        smsTemplate = smsTemplate
    )
}