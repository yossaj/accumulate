package com.lingoal.accumulate.ui.screens.lifts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingoal.accumulate.models.LiftEntry
import com.lingoal.accumulate.models.LiftSession
import com.lingoal.accumulate.repositories.LiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddLiftGoalViewModel @Inject constructor(
    private val liftRepository: LiftRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<AddLiftGoalUIState> = MutableStateFlow(AddLiftGoalUIState())
    val uiState: StateFlow<AddLiftGoalUIState> get() = _uiState

    fun setLiftName(liftName: String) = _uiState.update { it.copy(liftName = liftName) }

    fun setLiftType(liftTypes: LiftEntry.LiftTypes) = _uiState.update { it.copy(liftType = liftTypes) }

    fun setWeight(weight: String) = _uiState.update { it.copy(weight = weight.toFloatOrNull()) }

    fun setLiftGoal(liftGoal: Long) = _uiState.update { it.copy(liftGoalId = liftGoal) }

    fun addGoal(){
        val liftName = _uiState.value.liftName ?: return
        val liftType = _uiState.value.liftType ?: return
        val weight = _uiState.value.weight ?: return

        if (_uiState.value.liftSession == null){
            createSession()
        }

        _uiState.value.liftSession?.id?.let { sessionId ->
            val currentEntries = _uiState.value.liftEntries.toMutableList()
            currentEntries.add(
                LiftEntry(
                    sessionId = sessionId,
                    liftName = liftName,
                    liftType = liftType,
                    weightKg = weight,
                    reps = _uiState.value.reps,
                    sets = _uiState.value.sets
                )
            )

            _uiState.update { it.copy(liftEntries = currentEntries)}
        }

    }

    fun createSession(){
        val liftGoalId = _uiState.value.liftGoalId ?: return

        val liftSession = LiftSession(goalId = liftGoalId, date = LocalDateTime.now())

        _uiState.update { it.copy(liftSession = liftSession) }
    }

    fun saveSessionAndGoals(){
        val session = _uiState.value.liftSession ?: return

        viewModelScope.launch {
            val sessionId = liftRepository.insertLiftSession(session)

            Log.d("AddLiftGoal", sessionId.toString())
            val entries = _uiState.value.liftEntries
                .map { it.copy(sessionId = sessionId) }

            launch {
                liftRepository.insertLiftEntries(entries)
            }
        }
    }
}