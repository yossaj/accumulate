package com.lingoal.accumulate.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDateTime

@Dao
interface LiftSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: LiftSession): Long

    @Query("SELECT * FROM lift_sessions ORDER BY date DESC")
    fun getAllSessions(): List<LiftSession>

    @Query("SELECT * FROM lift_sessions WHERE date >= :dateTime AND goalId = :goalId ORDER BY date DESC LIMIT 1")
    suspend fun getRecentSessions(dateTime: LocalDateTime, goalId: Long): SessionWithLifts?

    @Query("SELECT * FROM lift_sessions WHERE goalId = :goalId")
    fun getSessionsForGoal(goalId: Long): List<LiftSession>
}
