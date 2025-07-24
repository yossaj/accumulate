package com.lingoal.accumulate.ui.screens.lifts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingoal.accumulate.models.GoalWithSessionsAndLifts
import com.lingoal.accumulate.repositories.LiftRepository
import com.lingoal.accumulate.ui.screens.time.goal.AddGoalUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LiftGoalViewModel @Inject constructor(
    private val liftRepository: LiftRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<LiftGoalUIState> = MutableStateFlow(LiftGoalUIState())
    val uiState: StateFlow<LiftGoalUIState> get() = _uiState

    init {
        val today = LocalDate.now()
        viewModelScope.launch {
            liftRepository.getGoalWithSessionsAndLifts(today).collectLatest { goalWithSessionsAndLifts ->
                _uiState.update { it.copy(goalWithSessionsAndLifts = goalWithSessionsAndLifts) }
            }
        }
    }

    fun setLiftGoal(goal: String?) = _uiState.update { it.copy(liftGoal = goal) }
}