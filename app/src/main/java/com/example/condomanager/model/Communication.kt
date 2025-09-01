package com.example.condomanager.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "communications")
data class Communication(
    @PrimaryKey(autoGenerate = true)
    var dbId: Long = 0,
    val id: String,
    val title: String,
    val content: String,
    val category: CommunicationCategory,
    @Embedded(prefix = "author_")
    val author: User,
    val date: Date,
    val complements: MutableList<Complement> = mutableListOf(),
    var isClosed: Boolean = false
) : Parcelable
