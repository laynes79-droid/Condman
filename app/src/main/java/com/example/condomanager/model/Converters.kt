package com.example.condomanager.model

import androidx.room.TypeConverter
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
    fun fromRole(value: String): Role {
        return Role.valueOf(value)
    }

    @TypeConverter
    fun roleToString(role: Role): String {
        return role.name
    }

    @TypeConverter
    fun fromCommunicationCategory(value: String): CommunicationCategory {
        return CommunicationCategory.valueOf(value)
    }

    @TypeConverter
    fun communicationCategoryToString(category: CommunicationCategory): String {
        return category.name
    }

    @TypeConverter
    fun fromComplementList(value: String?): MutableList<Complement> {
        val listType = object : TypeToken<MutableList<Complement>>() {}.type
        return Gson().fromJson(value, listType) ?: mutableListOf()
    }

    @TypeConverter
    fun toComplementList(list: MutableList<Complement>?): String {
        return Gson().toJson(list)
    }
}
