package com.lingoal.accumulate.ui.screens.lifts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingoal.accumulate.repositories.LiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LiftDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    liftRepository: LiftRepository,
): ViewModel() {
    private val _uiState: MutableStateFlow<LiftDetailUIState> = MutableStateFlow(LiftDetailUIState())
    val uiState: StateFlow<LiftDetailUIState> get() = _uiState

    private val localDate: LocalDate = run {
        val dateLong = savedStateHandle.get<Long>("date")
        dateLong?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } ?: LocalDate.now()
    }

    init {
        viewModelScope.launch {
            _uiState
                .map { it.selectedPeriod }
                .distinctUntilChanged()
                .flatMapLatest { period ->
                    combine(
                        liftRepository.getTotalLiftedPerDayBetween(localDate, period),
                        liftRepository.getTargetGoalTotalForPeriod(localDate, period)
                    ) { totals, targetWeight ->
                        _uiState.value.copy(
                            liftTotals = totals,
                            targetWeight = targetWeight
                        )
                    }
                }
                .collect { newState ->
                    _uiState.value = newState
                }
        }
    }

    fun setSelectedTimePeriod(timePeriod: LiftDetailUIState.TimePeriod) = _uiState.update { it.copy( selectedPeriod = timePeriod) }
}