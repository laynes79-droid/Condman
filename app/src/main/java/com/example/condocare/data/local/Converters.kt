package com.example.condocare.data.local

import androidx.room.TypeConverter
import com.example.condocare.data.model.Complement
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromComplementList(value: String?): List<Complement>? {
        val listType = object : TypeToken<List<Complement>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toComplementList(list: List<Complement>?): String? {
        return Gson().toJson(list)
    }
}
