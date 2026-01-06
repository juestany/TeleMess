package com.example.telemess.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.telemess.data.db.MissedCallDAO
import com.example.telemess.data.model.CallType
import com.example.telemess.data.model.MissedCallEntity
import kotlinx.coroutines.flow.Flow

class CallRepository(private val missedCallDAO: MissedCallDAO) {
    suspend fun insertMissedCall(newMissedCall: MissedCallEntity) {
        missedCallDAO.insertMissedCall(newMissedCall)
    }

    suspend fun deleteMissedCall(missedCall: MissedCallEntity) {
        missedCallDAO.deleteMissedCall(missedCall)
    }

    suspend fun getMissedCallById(missedCallId: Int): MissedCallEntity? {
        return missedCallDAO.getMissedCallById(missedCallId)
    }

    fun observeAllMissedCalls(): Flow<List<MissedCallEntity>> {
        return missedCallDAO.observeAllMissedCalls()
    }

    suspend fun getUndisplayedCalls(): List<MissedCallEntity> {
        return missedCallDAO.getUndisplayedCalls()
    }

    suspend fun markCallsAsDisplayed(ids: List<Int>) {
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
}