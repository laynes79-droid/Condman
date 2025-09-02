package com.example.condocare.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Complement(
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("author_name")
    val authorName: String,
    @SerializedName("timestamp")
    val timestamp: Date
)
