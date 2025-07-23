package com.lingoal.accumulate.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import java.time.LocalDate

@Dao
interface LiftGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(goal: LiftGoal): Long

    @Query("SELECT * FROM lift_goals ORDER BY startDate DESC")
    fun getAllGoals(): List<LiftGoal>

    @Query("SELECT * FROM lift_goals WHERE :date BETWEEN startDate AND endDate LIMIT 1")
    fun getGoalForDate(date: LocalDate): LiftGoal?

    @Transaction
    @Query("""
    SELECT * FROM lift_goals
    WHERE :date BETWEEN startDate AND endDate
    LIMIT 1
    """)
    fun getGoalWithSessionsAndLifts(date: LocalDate): GoalWithSessionsAndLifts?
}