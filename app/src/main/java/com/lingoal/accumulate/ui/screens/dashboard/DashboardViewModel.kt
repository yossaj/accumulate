package com.lingoal.accumulate.ui.screens.dashboard

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingoal.accumulate.models.Goal
import com.lingoal.accumulate.repositories.DatabaseTransactionProvider
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
    private val goalRepository: GoalRepository,
    private val databaseTransactionProvider: DatabaseTransactionProvider
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

    fun setHours(hours: String) = update { it.copy(hours = hours) }

    fun setMinutes(minutes: String) = update { it.copy(minutes = minutes) }

    fun setGoal(goal: Goal) = update { it.copy(selectedGoal = goal) }

    fun addTime(){
        val hours = uiState.value.hours ?: return
        val minutes = uiState.value.minutes ?: return
        val selectedGoal = uiState.value.selectedGoal ?: return

        val timeInMilliseconds = (hours.toLong() * 3600000) + (minutes.toLong() * 60000)

        selectedGoal.totalAccumulatedTime += timeInMilliseconds

        viewModelScope.launch {
            databaseTransactionProvider.runAsTransaction {
                goalRepository.updateGoal(selectedGoal)
            }
            setHours("")
            setMinutes("")
        }
    }

    fun startTimer(id: String) {
        val currentGoals = uiState.value.goals

        val updatedGoals = currentGoals.map { currentGoal ->
            if (currentGoal.id == id) {
                val goal = currentGoal.copy(
                    currentTimerStart = SystemClock.elapsedRealtime(),
                    currentTimerRunning = true
                )

                viewModelScope.launch {
                    databaseTransactionProvider.runAsTransaction {
                        goalRepository.updateGoal(goal)
                    }
                }

                goal
            } else {
                currentGoal
            }
        }
        update { it.copy(goals = updatedGoals) }
    }

    fun stopTimer(id: String) {
        val currentGoals = uiState.value.goals

        val updatedGoals = currentGoals.map { currentGoal ->
            if (currentGoal.id == id) {

                val updatedTime = currentGoal.totalAccumulatedTime +
                        (currentGoal.currentTimerStart?.let { SystemClock.elapsedRealtime() - it } ?: 0L)

                val goal = currentGoal.copy(
                    currentTimerStart = null,
                    currentTimerRunning = false,
                    totalAccumulatedTime = updatedTime
                )

                viewModelScope.launch {
                    databaseTransactionProvider.runAsTransaction {
                        goalRepository.updateGoal(goal)
                    }
                }

                goal
            } else {
                currentGoal
            }
        }
        update { it.copy(goals = updatedGoals) }
    }

    private fun update(block: (current: DashboardUIState) -> DashboardUIState) {
        viewModelScope.launch {
            _uiState.update { current ->
                block.invoke(current)
            }
        }
    }
}