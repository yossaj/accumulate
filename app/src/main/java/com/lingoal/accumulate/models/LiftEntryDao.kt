package com.lingoal.accumulate.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface LiftEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: LiftEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entries: List<LiftEntry>)

    @Query("UPDATE lift_entries SET sets = sets + 1 WHERE id = :entryId")
    suspend fun incrementSets(entryId: Long)

    @Query("UPDATE lift_entries SET sets = sets - 1 WHERE id = :entryId")
    suspend fun decrementSet(entryId: Long)

    @Query("SELECT * FROM lift_entries WHERE sessionId = :sessionId")
    fun getEntriesForSession(sessionId: Long): List<LiftEntry>

    @Query("""
        SELECT * FROM lift_entries
        WHERE timestamp BETWEEN :start AND :end
    """)
    fun getEntriesBetween(start: LocalDateTime, end: LocalDateTime): List<LiftEntry>

    @Query("""
        SELECT DISTINCT liftName FROM lift_entries
    """)
    fun getExistingExerciseNames(): Flow<List<String>>

    @Query("""
        SELECT SUM(weightKg * reps * sets)
        FROM lift_entries
        WHERE timestamp BETWEEN :start AND :end
    """)
    fun getTotalLiftedBetween(start: LocalDateTime, end: LocalDateTime): Float?

    @Query("""
    SELECT DATE(timestamp) as date, SUM(weightKg * reps * sets) as total
    FROM lift_entries
    WHERE timestamp BETWEEN :start AND :end
    GROUP BY DATE(timestamp)
    ORDER BY DATE(timestamp)
""")
    fun getTotalLiftedPerDayBetween(start: LocalDate, end: LocalDate): Flow<List<DailyLiftedTotal>>

}