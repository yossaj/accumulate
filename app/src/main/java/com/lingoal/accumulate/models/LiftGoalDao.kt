package com.lingoal.accumulate.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface LiftGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: LiftGoal): Long

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
    fun getGoalWithSessionsAndLifts(date: LocalDate): Flow<GoalWithSessionsAndLifts?>


    @Query("""
    SELECT * FROM lift_goals 
    WHERE :today BETWEEN startDate AND endDate 
    LIMIT 1
    """)
    fun getSelectedGoal(today: LocalDate = LocalDate.now()): Flow<LiftGoal?>


    @Query("""
    SELECT SUM(targetWeightKg)
    FROM lift_goals
    WHERE startDate <= :end
    AND endDate >= :start
    """)
    fun getTargetGoalTotalForPeriod(start: LocalDate, end: LocalDate): Flow<Int?>

    @Transaction
    @Query("""
    SELECT * FROM lift_sessions 
    WHERE goalId = :goalId 
    ORDER BY date DESC
    """)
    fun getSessionsWithLifts(goalId: Long): Flow<List<SessionWithLifts>>
}