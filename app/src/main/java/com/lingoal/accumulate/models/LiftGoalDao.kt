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
    suspend fun insert(goal: LiftGoal): Long

    @Query("SELECT * FROM goals ORDER BY startDate DESC")
    suspend fun getAllGoals(): List<LiftGoal>

    @Query("SELECT * FROM goals WHERE :date BETWEEN startDate AND endDate LIMIT 1")
    suspend fun getGoalForDate(date: LocalDate): LiftGoal?

    @Transaction
    @Query("""
    SELECT * FROM goals 
    WHERE :date BETWEEN startDate AND endDate 
    LIMIT 1
    """)
    suspend fun getGoalWithSessionsAndLifts(date: LocalDate): GoalWithSessionsAndLifts?
}