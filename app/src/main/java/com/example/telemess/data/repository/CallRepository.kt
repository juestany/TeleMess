package com.example.telemess.data.repository

import com.example.telemess.data.db.MissedCallDAO
import com.example.telemess.data.model.CallType
import com.example.telemess.data.model.MissedCallEntity
import com.example.telemess.ui.home.HomeMissedCallUi
import kotlinx.coroutines.flow.map

class CallRepository(private val missedCallDAO: MissedCallDAO) {

    suspend fun deleteMissedCall(missedCall: MissedCallEntity) {
        missedCallDAO.deleteMissedCall(missedCall)
    }

    suspend fun getMissedCallById(missedCallId: Long): MissedCallEntity? {
        return missedCallDAO.getMissedCallById(missedCallId)
    }

    suspend fun markCallsAsDisplayed(ids: List<Long>) {
        missedCallDAO.markCallsAsDisplayed(ids)
    }

    // Flow of all calls in descending timestamp order
    fun observeAllMissedCalls() = missedCallDAO.observeAllMissedCalls()
        .map { list ->
            // convert MissedCallEntity → HomeMissedCallUi
            list.map {
                HomeMissedCallUi(
                    id = it.id,
                    phoneNumber = it.phoneNumber,
                    timestamp = it.timestamp,
                    callType = it.callType,
                    isNew = !it.displayedToUser
                )
            }
        }
}