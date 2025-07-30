package com.lingoal.accumulate.ui.screens.lifts

import com.lingoal.accumulate.models.LiftGoal
import com.lingoal.accumulate.models.SessionWithLifts

data class LiftGoalUIState(
    val liftGoalId: String? = null,
    val liftGoal: LiftGoal? = null,
    val sessionsWithLifts: List<SessionWithLifts> = emptyList()
){
    val cumulativeTotal = sessionsWithLifts.sumOf { sessionWithList ->
            sessionWithList.lifts.sumOf { it.reps * it.sets * it.weightKg.toDouble() }
    }

    val canSaveGoal = liftGoalId?.toIntOrNull() != null
}