package com.lingoal.accumulate.ui.screens.lifts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingoal.accumulate.extensions.endofWeek
import com.lingoal.accumulate.extensions.startOfWeek
import com.lingoal.accumulate.models.GoalWithSessionsAndLifts
import com.lingoal.accumulate.models.LiftGoal
import com.lingoal.accumulate.models.LiftGoal.PeriodType
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

    private val _selectedDate: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> get() = _selectedDate

    init {
        viewModelScope.launch {
            selectedDate.collectLatest { selectedDate ->
                liftRepository.getGoalWithSessionsAndLifts(selectedDate).collectLatest { goalWithSessionsAndLifts ->
                    _uiState.update { it.copy(goalWithSessionsAndLifts = goalWithSessionsAndLifts) }
                }
            }

        }
    }

    fun incrementWeek() = _selectedDate.update { it.plusWeeks(1) }

    fun decrementWeek() = _selectedDate.update { it.minusWeeks(1) }

    fun setLiftGoal(goal: String?) = _uiState.update { it.copy(liftGoal = goal) }

    fun addGoal(){
        val liftGoalAmount = _uiState.value.liftGoal?.toIntOrNull() ?: return
        val startOfWeek = _selectedDate.value.startOfWeek
        val endOfWeek = _selectedDate.value.endofWeek

        val liftGoal = LiftGoal(id = 0, targetWeightKg = liftGoalAmount, startDate = startOfWeek, endDate = endOfWeek, periodType = PeriodType.Weekly)
        viewModelScope.launch {
            liftRepository.insertLiftGoal(liftGoal)
        }
    }
}