package com.example.telemess.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.telemess.data.model.MissedCallEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MissedCallDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissedCall(missedCall: MissedCallEntity)

    @Delete
    suspend fun deleteMissedCall(missedCall: MissedCallEntity)

    @Query("SELECT * FROM missed_calls WHERE timestamp = :timestamp LIMIT 1")
    suspend fun getByTimestamp(timestamp: Long): MissedCallEntity?

    @Query("""
        SELECT * FROM missed_calls
        WHERE missedCallId = :missedCallId
        LIMIT 1
    """)
    suspend fun getMissedCallById(missedCallId: Int): MissedCallEntity?

    @Query("""
        SELECT * FROM missed_calls
        ORDER BY timestamp DESC
    """)
    fun observeAllMissedCalls(): Flow<List<MissedCallEntity>>

    @Query("""
        SELECT * FROM missed_calls
        WHERE displayedToUser = 0
        ORDER BY timestamp DESC
    """)
    suspend fun getUndisplayedCalls(): List<MissedCallEntity> // calls that aren't yet displayed

    @Query("""
        UPDATE missed_calls
        SET displayedToUser = 1
        WHERE missedCallId IN (:ids)
    """)
    suspend fun markCallsAsDisplayed(ids: List<Int>)
}