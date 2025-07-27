package com.lingoal.accumulate.ui.screens.lifts

import com.lingoal.accumulate.models.GoalWithSessionsAndLifts

data class LiftGoalUIState(
    val liftGoal: String? = null,
    val goalWithSessionsAndLifts: GoalWithSessionsAndLifts? = null
){
    val cumulativeTotal = goalWithSessionsAndLifts
        ?.sessionsWithLifts?.sumOf { sessionWithList ->
            sessionWithList.lifts.sumOf { it.reps * it.sets * it.weightKg.toDouble() }
        } ?: 0.0

    val canSaveGoal = liftGoal?.toIntOrNull() != null
}