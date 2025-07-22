package com.lingoal.accumulate

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lingoal.accumulate.models.Goal
import com.lingoal.accumulate.models.GoalDao
import com.lingoal.accumulate.models.LiftEntry
import com.lingoal.accumulate.models.LiftEntryDao
import com.lingoal.accumulate.models.LiftGoal
import com.lingoal.accumulate.models.LiftGoalDao
import com.lingoal.accumulate.models.LiftSession
import com.lingoal.accumulate.models.LiftSessionDao

@Database(entities = [Goal::class, LiftGoal::class, LiftSession::class, LiftEntry::class], version = AppDatabase.VERSION)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        const val VERSION = 2
    }

    abstract fun goalDao(): GoalDao
    abstract fun liftGoalDao(): LiftGoalDao
    abstract fun liftSessionDao(): LiftSessionDao
    abstract fun liftEntryDao(): LiftEntryDao
}