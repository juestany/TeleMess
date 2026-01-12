package com.example.telemess.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telemess.data.repository.CallRepository
import com.example.telemess.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val settingsRepository: SettingsRepository,
    private val callRepository: CallRepository
) : ViewModel() {

    val quietHoursEnabled: StateFlow<Boolean> =
        settingsRepository.observeSettings()
            .map { it?.isEnabled ?: true }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    // Live updates from DB
    val missedCalls: StateFlow<List<HomeMissedCallUi>> =
        callRepository.observeAllMissedCalls()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Mark as read in DB
    fun markAsRead(callId: Int) {
        viewModelScope.launch {
            val call = callRepository.getMissedCallById(callId)
            if (call != null && !call.displayedToUser) {
                callRepository.markCallsAsDisplayed(listOf(call.id))
            }
        }
    }

    fun toggleQuietHours(enabled: Boolean) {
        viewModelScope.launch {
            val settings = settingsRepository.getOrCreateDefault()
            settingsRepository.saveSettings(settings.copy(isEnabled = enabled))
        }
    }
}