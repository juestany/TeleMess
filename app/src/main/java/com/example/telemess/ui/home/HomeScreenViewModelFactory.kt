package com.example.telemess.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telemess.data.repository.CallRepository
import com.example.telemess.data.repository.SettingsRepository

class HomeScreenViewModelFactory(
    private val settingsRepo: SettingsRepository,
    private val callRepo: CallRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(settingsRepo, callRepo) as T
    }
}