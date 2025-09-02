package com.example.condocare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateCommunicationRequest(
    val title: String,
    val message: String,
    @SerializedName("is_emergency")
    val isEmergency: Boolean
)
