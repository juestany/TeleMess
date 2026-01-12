package com.example.telemess.data.repository

import com.example.telemess.data.db.MissedCallDAO
import com.example.telemess.data.model.CallType
import com.example.telemess.data.model.MissedCallEntity
import com.example.telemess.ui.home.HomeMissedCallUi
import kotlinx.coroutines.flow.map

class CallRepository(private val missedCallDAO: MissedCallDAO) {
    suspend fun insertMissedCall(newMissedCall: MissedCallEntity) {
        missedCallDAO.insertMissedCall(newMissedCall)
    }

    suspend fun deleteMissedCall(missedCall: MissedCallEntity) {
        missedCallDAO.deleteMissedCall(missedCall)
    }

    suspend fun getMissedCallById(missedCallId: Long): MissedCallEntity? {
        return missedCallDAO.getMissedCallById(missedCallId)
    }

    suspend fun getUndisplayedCalls(): List<MissedCallEntity> {
        return missedCallDAO.getUndisplayedCalls()
    }

    suspend fun markCallsAsDisplayed(ids: List<Long>) {
        missedCallDAO.markCallsAsDisplayed(ids)
    }

    /**
     * Convenience method used by services (CallScreening / Foreground)
     */
    suspend fun saveMissedCall(
        phoneNumber: String,
        callType: CallType
    ) {
        missedCallDAO.insertMissedCall(
            MissedCallEntity(
                id = 0,
                phoneNumber = phoneNumber,
                timestamp = System.currentTimeMillis(),
                callType = callType,
                smsSent = false,
                displayedToUser = false
            )
        )
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