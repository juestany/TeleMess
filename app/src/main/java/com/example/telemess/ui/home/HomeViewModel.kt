package com.example.telemess.ui.home

import androidx.lifecycle.ViewModel
import com.example.telemess.data.model.CallType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeScreenViewModel : ViewModel() {

    private val _quietHoursEnabled = MutableStateFlow(true)
    val quietHoursEnabled: StateFlow<Boolean> = _quietHoursEnabled

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _sendSmsEnabled = MutableStateFlow(false)
    val sendSmsEnabled: StateFlow<Boolean> = _sendSmsEnabled

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
    val missedCalls: StateFlow<List<HomeMissedCallUi>> = _missedCalls

    fun toggleQuietHours(enabled: Boolean) {
        _quietHoursEnabled.value = enabled
    }

    fun updateMessage(text: String) {
        _message.value = text
    }

    fun toggleSendSms() {
        _sendSmsEnabled.value = !_sendSmsEnabled.value
    }

    fun markAsRead(callId: Int) {
        _missedCalls.value = _missedCalls.value.map {
            if (it.id == callId) it.copy(isNew = false) else it
        }
    }
}