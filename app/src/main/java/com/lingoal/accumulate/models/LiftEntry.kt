package com.lingoal.accumulate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "lift_entries",
    foreignKeys = [ForeignKey(
        entity = LiftSession::class,
        parentColumns = ["id"],
        childColumns = ["sessionId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LiftEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var sessionId: Long,
    val liftName: String,
    val liftType: LiftTypes,
    val weightKg: Float,
    val reps: Int,
    val sets: Int,
    val timestamp: LocalDateTime = LocalDateTime.now()
){
    enum class LiftTypes(){
        Pull,
        Push,
        Legs
    }
}