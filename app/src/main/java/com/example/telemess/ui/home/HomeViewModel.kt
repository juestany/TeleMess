package com.example.telemess.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telemess.data.model.CallType
import com.example.telemess.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val quietHoursEnabled: StateFlow<Boolean> =
        settingsRepository.observeSettings()
            .map { it?.isEnabled ?: true }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

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

    fun toggleQuietHours(enabled: Boolean) {
        viewModelScope.launch {
            val settings = settingsRepository.getOrCreateDefault()
            settingsRepository.saveSettings(settings.copy(isEnabled = enabled))
        }
    }

    val missedCalls: StateFlow<List<HomeMissedCallUi>> = _missedCalls

    fun markAsRead(callId: Int) {
        _missedCalls.value = _missedCalls.value.map {
            if (it.id == callId) it.copy(isNew = false) else it
        }
    }
}