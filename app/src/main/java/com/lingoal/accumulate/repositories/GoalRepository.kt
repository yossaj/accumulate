package com.lingoal.accumulate.repositories

import com.lingoal.accumulate.models.Goal
import com.lingoal.accumulate.models.GoalDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoalRepository @Inject constructor(
    private val goalDao: GoalDao
) {
    fun getAllGoals(): Flow<List<Goal>> {
        return goalDao.getAllGoals()
    }

    fun insertGoal(goal: Goal) {
        goalDao.insetGoal(goal)
    }

    fun deleteGoal(goal: Goal) {
        goalDao.deleteGoal(goal)
    }
}