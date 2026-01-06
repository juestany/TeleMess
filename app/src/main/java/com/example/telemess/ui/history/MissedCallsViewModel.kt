package com.example.telemess.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telemess.data.model.MissedCallEntity
import com.example.telemess.data.repository.CallRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MissedCallsViewModel(
    private val callRepository: CallRepository
) : ViewModel() {

    /**
     * Lista wszystkich połączeń
     */
    val missedCalls: StateFlow<List<MissedCallEntity>> =
        callRepository.observeAllMissedCalls()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    /**
     * Oznacza połączenia jako wyświetlone (po wejściu na ekran)
     */
    fun markVisibleCallsAsDisplayed(calls: List<MissedCallEntity>) {
        val ids = calls
            .filter { !it.displayedToUser }
            .map { it.id }

        if (ids.isEmpty()) return

        viewModelScope.launch {
            callRepository.markCallsAsDisplayed(ids)
        }
    }

    fun deleteCall(call: MissedCallEntity) {
        viewModelScope.launch {
            callRepository.deleteMissedCall(call)
        }
    }
}