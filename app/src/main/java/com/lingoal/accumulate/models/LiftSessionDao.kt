package com.lingoal.accumulate.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LiftSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: LiftSession): Long

    @Query("SELECT * FROM lift_sessions ORDER BY date DESC")
    suspend fun getAllSessions(): List<LiftSession>

    @Query("SELECT * FROM lift_sessions WHERE goalId = :goalId")
    suspend fun getSessionsForGoal(goalId: Long): List<LiftSession>
}
