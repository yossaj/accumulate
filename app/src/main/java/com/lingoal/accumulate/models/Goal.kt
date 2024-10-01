package com.lingoal.accumulate.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString().uppercase(),
    val name: String,
    val goalTime: Float,
    val totalAccumulatedTime: Float,
    var currentTimerStart: Long? = null,
    var currentTimerRunning: Boolean = false,
) {

    val progress: Float
        get() {
           return totalAccumulatedTime / goalTime
        }
}