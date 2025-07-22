package com.lingoal.accumulate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "lift_sessions",
    foreignKeys = [ForeignKey(
        entity = LiftGoal::class,
        parentColumns = ["id"],
        childColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LiftSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val goalId: Long? = null,
    val note: String? = null
)