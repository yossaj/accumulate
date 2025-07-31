package com.lingoal.accumulate.ui.screens.lifts

import com.lingoal.accumulate.models.LiftEntry
import com.lingoal.accumulate.models.LiftEntry.LiftTypes
import com.lingoal.accumulate.models.LiftSession

data class AddLiftGoalUIState(
    val isUpdate: Boolean = false,
    val liftName: String? = null,
    val liftType: LiftTypes? = null,
    val weight: Float? = 10f,
    val reps: Int = 10,
    val sets: Int = 0,
    val liftSession: LiftSession? = null,
    val liftEntries: List<LiftEntry> = emptyList(),
    val existingLifts: List<LiftEntry> = emptyList()
) {
    val lifts = liftEntries + existingLifts
}