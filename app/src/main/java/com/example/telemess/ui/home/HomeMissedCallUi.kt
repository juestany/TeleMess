package com.example.telemess.ui.home

import com.example.telemess.data.model.CallType

data class HomeMissedCallUi(
    val id: Long,
    val phoneNumber: String,
    val timestamp: Long,
    val callType: CallType,
    val isNew: Boolean
)