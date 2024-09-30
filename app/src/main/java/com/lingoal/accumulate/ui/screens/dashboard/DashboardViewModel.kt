package com.lingoal.accumulate.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingoal.accumulate.repositories.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DashboardUIState> =
        MutableStateFlow(DashboardUIState())
    val uiState: StateFlow<DashboardUIState> get() = _uiState

    init {
        viewModelScope.launch {
            goalRepository.getAllGoals().collectLatest { goals ->
                update { it.copy(goals = goals) }
            }
        }
    }

    private fun update(block: (current: DashboardUIState) -> DashboardUIState) {
        viewModelScope.launch {
            _uiState.update { current ->
                block.invoke(current)
            }
        }
    }

    fun formatElapsedTime(milliseconds: Float): String {
        val totalMinutes = (milliseconds / 1000 / 60).toInt() 
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return "${hours}hr ${minutes} min"
    }

}