package com.example.telemess.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.telemess.data.model.QuietHoursSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface QuietHoursDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(settings: QuietHoursSettings) // suspend: not on main thread

    @Query("SELECT * FROM QuietHoursSettings LIMIT 1")
    fun observeSettings(): Flow<QuietHoursSettings?> // automatic reaction to changes

    @Query("SELECT * FROM QuietHoursSettings LIMIT 1")
    suspend fun getSettings(): QuietHoursSettings?

    @Query("DELETE FROM QuietHoursSettings")
    suspend fun clear()
}
