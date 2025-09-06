package com.lingoal.accumulate.ui.screens.lifts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lingoal.accumulate.repositories.LiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class LiftDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    liftRepository: LiftRepository,
): ViewModel() {
    private val _uiState: MutableStateFlow<LiftDetailUIState> = MutableStateFlow(LiftDetailUIState())
    val uiState: StateFlow<LiftDetailUIState> get() = _uiState

    init {
        val dateLong = savedStateHandle.get<Long>("date")
        val localDate = dateLong?.let {
            Instant.ofEpochMilli(dateLong)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }  ?: LocalDate.now()

        viewModelScope.launch {
            liftRepository.getTotalLiftedPerDayBetween(localDate).let { totalPerDay ->
                _uiState.update { it.copy(liftTotals = totalPerDay) }
            }
        }

        viewModelScope.launch {
            liftRepository.getCurrentGoal(localDate).collectLatest { currentGoal ->
                _uiState.update { it.copy(currentGoal = currentGoal?.targetWeightKg) }
            }
        }

    }
}