package com.example.telemess.ui.permissions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PermissionsViewModel : ViewModel() {
    var refreshTrigger by mutableStateOf(0)
        private set

    fun refresh() {
        refreshTrigger++
    }
}