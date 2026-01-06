package com.example.telemess.ui.quiet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telemess.data.repository.SettingsRepository

class QuietHoursViewModelFactory(
    private val repository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuietHoursViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuietHoursViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}