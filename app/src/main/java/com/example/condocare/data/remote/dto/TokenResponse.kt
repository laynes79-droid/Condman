package com.example.condocare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("role")
    val role: String
)
