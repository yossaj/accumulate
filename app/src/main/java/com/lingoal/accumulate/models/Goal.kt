package com.lingoal.accumulate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString().uppercase(),
    val name: String,
    val goalTime: Float,
    val totalAccumulatedTime: Float
) {

    val progress: Float
        get() {
           return totalAccumulatedTime / goalTime
        }
}