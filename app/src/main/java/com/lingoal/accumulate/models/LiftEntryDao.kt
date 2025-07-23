package com.lingoal.accumulate.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDateTime

@Dao
interface LiftEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: LiftEntry): Long

    @Query("SELECT * FROM lift_entries WHERE sessionId = :sessionId")
    fun getEntriesForSession(sessionId: Long): List<LiftEntry>

    @Query("""
        SELECT * FROM lift_entries
        WHERE timestamp BETWEEN :start AND :end
    """)
    fun getEntriesBetween(start: LocalDateTime, end: LocalDateTime): List<LiftEntry>

    @Query("""
        SELECT SUM(weightKg * reps * sets)
        FROM lift_entries
        WHERE timestamp BETWEEN :start AND :end
    """)
    fun getTotalLiftedBetween(start: LocalDateTime, end: LocalDateTime): Float?
}