package com.lingoal.accumulate.ui.screens.lifts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingoal.accumulate.extensions.endofWeek
import com.lingoal.accumulate.extensions.startOfWeek
import com.lingoal.accumulate.models.LiftEntry
import com.lingoal.accumulate.models.LiftGoal
import com.lingoal.accumulate.models.LiftGoal.PeriodType
import com.lingoal.accumulate.repositories.LiftRepository
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
                liftRepository.getCurrentGoal(selectedDate).collectLatest { liftGoal  ->
                    _uiState.update { it.copy(liftGoal = liftGoal) }
                    liftGoal?.let {
                        launch {
                            liftRepository.getSessionsWithLifts(liftGoal.id).collectLatest { sessionsWithLifts ->
                                _uiState.update { it.copy(sessionsWithLifts = sessionsWithLifts) }
                            }
                        }
                    }
                }
            }
        }
    }

    fun incrementWeek() = _selectedDate.update { it.plusWeeks(1) }

    fun decrementWeek() = _selectedDate.update { it.minusWeeks(1) }

    fun setLiftGoal(goal: String?) = _uiState.update { it.copy(liftGoalId = goal) }

    fun addGoal(){
        val liftGoalAmount = _uiState.value.liftGoalId?.toIntOrNull() ?: return
        val startOfWeek = _selectedDate.value.startOfWeek
        val endOfWeek = _selectedDate.value.endofWeek

        val liftGoal = LiftGoal(id = 0, targetWeightKg = liftGoalAmount, startDate = startOfWeek, endDate = endOfWeek, periodType = PeriodType.Weekly)
        viewModelScope.launch {
            liftRepository.insertLiftGoal(liftGoal)
        }
    }

    fun incrementSet(liftEntry: LiftEntry){
        viewModelScope.launch {
            liftRepository.incrementSets(liftEntry.id)
        }
    }

    fun decrementSet(liftEntry: LiftEntry){
        viewModelScope.launch {
            liftRepository.decrementSet(liftEntry.id)
        }
    }
}