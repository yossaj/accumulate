package com.lingoal.accumulate

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lingoal.accumulate.models.Goal
import com.lingoal.accumulate.models.GoalDao

@Database(entities = [Goal::class], version = AppDatabase.VERSION)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun goalDao(): GoalDao
}