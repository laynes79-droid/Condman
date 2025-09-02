package com.example.condocare.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.condocare.data.local.Converters
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "communications")
@TypeConverters(Converters::class)
data class Communication(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("is_emergency")
    val isEmergency: Boolean,
    @SerializedName("status")
    val status: String, // "Open" or "Closed"
    @SerializedName("author_id")
    val authorId: Int,
    @SerializedName("timestamp")
    val timestamp: Date,
    @SerializedName("complements")
    val complements: List<Complement>
)
