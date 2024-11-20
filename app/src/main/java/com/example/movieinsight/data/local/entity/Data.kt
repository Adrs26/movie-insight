package com.example.movieinsight.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Data(
    @PrimaryKey
    val id: Int,
    val posterPath: String,
    val averageVote: Double,
    val dataType: String
)
