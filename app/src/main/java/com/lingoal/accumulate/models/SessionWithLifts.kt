package com.lingoal.accumulate.models

import androidx.room.Embedded
import androidx.room.Relation

data class SessionWithLifts(
    @Embedded val session: LiftSession,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val lifts: List<LiftEntry>
)


val SessionWithLifts.cumulativeWeight: Double
    get() = this.lifts.sumOf { it.weightKg.toDouble() * (it.reps * it.sets).toDouble() }