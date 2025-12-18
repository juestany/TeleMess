package com.example.telemess.data.db

import androidx.room.TypeConverter
import com.example.telemess.data.model.CallType

class CallTypeConverter {
    @TypeConverter
    fun fromCallType(type: CallType): String {
        return type.name
    }

    @TypeConverter
    fun toCallType(value: String): CallType {
        return CallType.valueOf(value)
    }
}