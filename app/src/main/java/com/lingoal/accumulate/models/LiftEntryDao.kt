package com.lingoal.accumulate.models

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDateTime

interface LiftEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: LiftEntry): Long

    @Query("SELECT * FROM lift_entries WHERE sessionId = :sessionId")
    suspend fun getEntriesForSession(sessionId: Long): List<LiftEntry>

    @Query("""
        SELECT * FROM lift_entries 
        WHERE timestamp BETWEEN :start AND :end
    """)
    suspend fun getEntriesBetween(start: LocalDateTime, end: LocalDateTime): List<LiftEntry>

    @Query("""
        SELECT SUM(weightKg * reps * sets) 
        FROM lift_entries 
        WHERE timestamp BETWEEN :start AND :end
    """)
    suspend fun getTotalLiftedBetween(start: LocalDateTime, end: LocalDateTime): Float?
}