package com.lingoal.accumulate.ui.screens.time.goal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingoal.accumulate.repositories.DatabaseTransactionProvider
import com.lingoal.accumulate.repositories.GoalRepository
import com.lingoal.accumulate.ui.screens.time.dashboard.DashboardUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val databaseTransactionProvider: DatabaseTransactionProvider,
    private val goalRepository: GoalRepository
) : ViewModel() {


    private val _uiState: MutableStateFlow<GoalDetailUIState> =
        MutableStateFlow(GoalDetailUIState())
    val uiState: StateFlow<GoalDetailUIState> get() = _uiState

    init {
        savedStateHandle.get<String>("goalId")?.let { goalId ->
            viewModelScope.launch {
                databaseTransactionProvider.runAsTransaction {
                    goalRepository.getGoal(goalId)?.let { goal ->
                        update { it.copy(goal = goal) }
                    }
                }
            }
        }
    }

    private fun update(block: (current: GoalDetailUIState) -> GoalDetailUIState) {
        viewModelScope.launch {
            _uiState.update { current ->
                block.invoke(current)
            }
        }
    }
}