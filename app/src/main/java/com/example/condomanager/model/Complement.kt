package com.example.condomanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Complement(
    val id: String,
    val author: User,
    val content: String,
    val date: Date
) : Parcelable
