package com.lingoal.accumulate.models

import androidx.room.Embedded
import androidx.room.Relation

data class GoalWithSessionsAndLifts(
    @Embedded val goal: LiftGoal,
    @Relation(
        entity = LiftSession::class,
        parentColumn = "id",
        entityColumn = "goalId"
    )
    val sessionsWithLifts: List<SessionWithLifts>
)
