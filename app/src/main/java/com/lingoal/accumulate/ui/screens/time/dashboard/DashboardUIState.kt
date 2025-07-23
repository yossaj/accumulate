package com.lingoal.accumulate.ui.screens.time.dashboard

import com.lingoal.accumulate.models.Goal

data class DashboardUIState(
    val goals: List<Goal> = mutableListOf(),
    val hours: String? = null,
    val minutes: String? = null,
    val selectedGoal: Goal? = null,
    val videoUrlString: String? = null,
    val videoDurationString: String? = null,
    val videoDuration: Long? = null,
){
    val canAddTime = !hours.isNullOrEmpty() && !minutes.isNullOrEmpty()
    val canSaveVideoDuration = videoDuration != null
}