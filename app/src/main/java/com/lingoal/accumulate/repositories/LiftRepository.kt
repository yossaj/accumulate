package com.lingoal.accumulate.repositories

import com.lingoal.accumulate.extensions.toEndOfDayString
import com.lingoal.accumulate.models.DailyLiftedTotal
import com.lingoal.accumulate.models.LiftEntry
import com.lingoal.accumulate.models.LiftEntryDao
import com.lingoal.accumulate.models.LiftGoal
import com.lingoal.accumulate.models.LiftGoalDao
import com.lingoal.accumulate.models.LiftSession
import com.lingoal.accumulate.models.LiftSessionDao
import com.lingoal.accumulate.models.SessionWithLifts
import com.lingoal.accumulate.ui.screens.lifts.LiftDetailUIState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class LiftRepository @Inject constructor(
    private val liftGoalDao: LiftGoalDao,
    private val liftSessionDao: LiftSessionDao,
    private val liftEntryDao: LiftEntryDao
) {
    suspend fun insertLiftGoal(liftGoal: LiftGoal) = liftGoalDao.insert(liftGoal)

    suspend fun insertLiftSession(liftSession: LiftSession) = liftSessionDao.insert(liftSession)

    suspend fun insertLiftEntry(liftEntry: LiftEntry) = liftEntryDao.insert(liftEntry)

    suspend fun insertLiftEntries(entries: List<LiftEntry>) = liftEntryDao.insert(entries)

    suspend fun incrementSets(entryId: Long) = liftEntryDao.incrementSets(entryId)

    suspend fun decrementSet(entryId: Long) = liftEntryDao.decrementSet(entryId)

    fun getCurrentGoal(date: LocalDate): Flow<LiftGoal?> =  liftGoalDao.getSelectedGoal(date)

    fun getTargetGoalTotalForPeriod(selectedDate: LocalDate,  period: LiftDetailUIState.TimePeriod): Flow<Int?> {
        val (start, end) = getDateRangeForPeriod(period, selectedDate)
         return liftGoalDao.getTargetGoalTotalForPeriod(start, end)
    }

    fun getSessionsWithLifts(goalId: Long): Flow<List<SessionWithLifts>> = liftGoalDao.getSessionsWithLifts(goalId)

    suspend fun getRecentSessions(dateTime: LocalDateTime, goalId: Long) = liftSessionDao.getRecentSessions(dateTime, goalId)

    fun getExistingExerciseNames(): Flow<List<String>> = liftEntryDao.getExistingExerciseNames()

    fun getTotalLiftedPerDayBetween(selectedDate: LocalDate,  period: LiftDetailUIState.TimePeriod): Flow<List<DailyLiftedTotal>> {
        val (start, end) = getDateRangeForPeriod(period, selectedDate)

        return liftEntryDao.getTotalLiftedPerDayBetween(start, end)
    }

    fun getTotalLiftedForTypeBetween(selectedDate: LocalDate,  period: LiftDetailUIState.TimePeriod, liftType: LiftEntry.LiftTypes): Flow<Float> {
        val (start, end) = getDateRangeForPeriodAsLocalDateTime(period, selectedDate)

        return liftEntryDao.getTotalLiftedForTypeBetween(start, end, liftType)
    }


    private fun getDateRangeForPeriod(
        period: LiftDetailUIState.TimePeriod,
        selectedDate: LocalDate
    ): Pair<LocalDate, LocalDate> = when (period) {
        LiftDetailUIState.TimePeriod.Weekly -> {
            val start = selectedDate.with(DayOfWeek.MONDAY)
            val end = start.plusDays(6)
            start to end
        }

        LiftDetailUIState.TimePeriod.Monthly -> {
            val start = selectedDate.withDayOfMonth(1)
            val end = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth())
            start to end
        }

        LiftDetailUIState.TimePeriod.Yearly -> {
            val start = selectedDate.withDayOfYear(1)
            val end = selectedDate.withDayOfYear(selectedDate.lengthOfYear())
            start to end
        }
    }

    private fun getDateRangeForPeriodAsLocalDateTime(
        period: LiftDetailUIState.TimePeriod,
        selectedDate: LocalDate
    ): Pair<LocalDateTime, LocalDateTime> {
        val (start, end) = getDateRangeForPeriod(period, selectedDate)
        return start.atStartOfDay() to end.toEndOfDayString()
    }
}

