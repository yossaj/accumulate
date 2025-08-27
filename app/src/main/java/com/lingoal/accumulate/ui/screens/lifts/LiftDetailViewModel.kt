package com.lingoal.accumulate.ui.screens.lifts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lingoal.accumulate.repositories.LiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LiftDetailViewModel @Inject constructor(
    liftRepository: LiftRepository,
): ViewModel() {
    private val _uiState: MutableStateFlow<LiftDetailUIState> = MutableStateFlow(LiftDetailUIState())
    val uiState: StateFlow<LiftDetailUIState> get() = _uiState

    init {
        viewModelScope.launch {
            liftRepository.getTotalLiftedPerDayBetween(LocalDate.now()).let { totalPerDay ->
                _uiState.update { it.copy(liftTotals = totalPerDay) }
            }
        }

    }
}