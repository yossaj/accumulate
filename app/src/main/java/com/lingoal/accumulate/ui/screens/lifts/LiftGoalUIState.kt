package com.lingoal.accumulate.ui.screens.lifts

import com.lingoal.accumulate.models.GoalWithSessionsAndLifts

data class LiftGoalUIState(
    val liftGoal: String? = null,
    val goalWithSessionsAndLifts: GoalWithSessionsAndLifts? = null
){

    val canSaveGoal = liftGoal?.toIntOrNull() != null
}