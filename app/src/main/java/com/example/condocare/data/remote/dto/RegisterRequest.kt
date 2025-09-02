package com.example.condocare.data.remote.dto

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val apartment: String,
    val role: String
)
