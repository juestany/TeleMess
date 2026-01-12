package com.example.telemess.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missed_calls")
data class MissedCallEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "missedCallId")
    val id: Long = 0L,

    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "callType")
    val callType: CallType,

    @ColumnInfo(name = "smsSent")
    val smsSent: Boolean,

    @ColumnInfo(name = "displayedToUser")
    val displayedToUser: Boolean
)