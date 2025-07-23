package com.lingoal.accumulate.ui.screens.time.goal

data class AddGoalUIState(
    val name: String? = null,
    val cumulativeHours: String? = null
){
    val canSave = !name.isNullOrEmpty() && !cumulativeHours.isNullOrEmpty()
}