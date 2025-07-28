package com.lingoal.accumulate.ui.screens.lifts

import com.lingoal.accumulate.models.LiftEntry
import com.lingoal.accumulate.models.LiftEntry.LiftTypes
import com.lingoal.accumulate.models.LiftSession

data class AddLiftGoalUIState(
    val liftName: String? = null,
    val liftType: LiftTypes? = null,
    val liftGoalId: Long? = null,
    val weight: Float? = 10f,
    val reps: Int = 10,
    val sets: Int = 0,
    val liftSession: LiftSession? = null,
    val liftEntries: List<LiftEntry> = emptyList()
) {
}