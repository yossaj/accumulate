package com.lingoal.accumulate.ui.screens.lifts

import com.lingoal.accumulate.models.LiftEntry
import com.lingoal.accumulate.models.LiftEntry.LiftTypes

data class AddLiftGoalUIState(
    val exerciseName: String? = null,
    val exerciseType: LiftTypes? = null
) {
}