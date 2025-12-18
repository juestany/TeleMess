package com.example.telemess.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.telemess.data.model.MissedCallEntity
import com.example.telemess.data.model.QuietHoursSettings

@Database(
    entities = [
        MissedCallEntity::class,
        QuietHoursSettings::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(CallTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun missedCallDao(): MissedCallDAO
    abstract fun quietHoursDao(): QuietHoursDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "telemess_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}