package com.lingoal.accumulate.ui.screens.dashboard

import com.lingoal.accumulate.models.Goal

data class DashboardUIState(
    val goals: List<Goal> = mutableListOf(),
    val hours: String? = null,
    val minutes: String? = null,

)