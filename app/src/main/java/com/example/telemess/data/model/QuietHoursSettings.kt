package com.example.telemess.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuietHoursSettings (
    @PrimaryKey
    @ColumnInfo(name = "quietHoursId")
    val id: Int,

    @ColumnInfo(name = "enabled")
    val isEnabled: Boolean,

    @ColumnInfo(name = "startHour")
    val startHour: Int,

    @ColumnInfo(name = "startMinute")
    val startMinute: Int,

    @ColumnInfo(name = "endHour")
    val endHour: Int,

    @ColumnInfo(name = "endMinute")
    val endMinute: Int,

    @ColumnInfo(name = "autoSmsEnabled")
    val isAutoSmsEnabled: Boolean,

    @ColumnInfo(name = "smsTemplate")
    val smsTemplate: String
)