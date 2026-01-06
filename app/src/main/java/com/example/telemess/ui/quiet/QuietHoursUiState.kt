package com.example.telemess.ui.quiet

data class QuietHoursUiState(
    val enabled: Boolean = false,
    val startHour: Int = 22,
    val startMinute: Int = 0,
    val endHour: Int = 7,
    val endMinute: Int = 0,
    val autoSmsEnabled: Boolean = true,
    val smsTemplate: String = ""
)