package com.lingoal.accumulate.repositories

import com.lingoal.accumulate.models.LiftEntry
import com.lingoal.accumulate.models.LiftEntryDao
import com.lingoal.accumulate.models.LiftGoal
import com.lingoal.accumulate.models.LiftGoalDao
import com.lingoal.accumulate.models.LiftSession
import com.lingoal.accumulate.models.LiftSessionDao
import jakarta.inject.Inject
import java.time.LocalDate

class LiftRepository @Inject constructor(
    private val liftGoalDao: LiftGoalDao,
    private val liftSessionDao: LiftSessionDao,
    private val liftEntryDao: LiftEntryDao
) {
    fun insertLiftGoal(liftGoal: LiftGoal) = liftGoalDao.insert(liftGoal)

    fun insertLiftSession(liftSession: LiftSession) = liftSessionDao.insert(liftSession)

    fun insertLiftEntry(liftEntry: LiftEntry) = liftEntryDao.insert(liftEntry)

    fun getGoalWithSessionsAndLifts(date: LocalDate) = liftGoalDao.getGoalWithSessionsAndLifts(date)
}