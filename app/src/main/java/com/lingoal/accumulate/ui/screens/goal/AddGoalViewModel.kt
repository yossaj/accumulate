package com.lingoal.accumulate.ui.screens.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingoal.accumulate.models.Goal
import com.lingoal.accumulate.repositories.DatabaseTransactionProvider
import com.lingoal.accumulate.repositories.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val databaseTransactionProvider: DatabaseTransactionProvider
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddGoalUIState> =
        MutableStateFlow(AddGoalUIState())
    val uiState: StateFlow<AddGoalUIState> get() = _uiState

    fun addGoal(){
        val name = uiState.value.name ?: return
        val cumulativeTimeString = uiState.value.cumulativeHours ?: return

        val cumulativeTime = cumulativeTimeString.toFloat()

        val goal = Goal(
            name = name,
            goalTime = cumulativeTime,
            totalAccumulatedTime = 0f
        )

        viewModelScope.launch {
            databaseTransactionProvider.runAsTransaction {
                goalRepository.insertGoal(goal)
            }
        }
    }

    fun updateName(name: String) = update { it.copy(name = name) }

    fun updateCumulativeHours(time: String) = update { it.copy(cumulativeHours = time) }

    private fun update(block: (current: AddGoalUIState) -> AddGoalUIState) {
        viewModelScope.launch {
            _uiState.update { current ->
                block.invoke(current)
            }
        }
    }
}