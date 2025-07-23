package com.lingoal.accumulate.helpers

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lingoal.accumulate.AppDatabase
import com.lingoal.accumulate.constants.DatabaseConstants

object RoomHelper {

    fun getDatabase(application: Application): AppDatabase {
        val db = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            DatabaseConstants.APP_DB_NAME
        )
            .addMigrations(
                MIGRATION_1_2,
            )
            .build()

        return db
    }


    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create table: lift_goals
            database.execSQL("""
            CREATE TABLE IF NOT EXISTS `lift_goals` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `targetWeightKg` INTEGER NOT NULL,
                `startDate` TEXT NOT NULL,
                `endDate` TEXT NOT NULL,
                `periodType` TEXT NOT NULL
            )
        """.trimIndent())

            // Create table: lift_sessions
            database.execSQL("""
            CREATE TABLE IF NOT EXISTS `lift_sessions` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `date` TEXT NOT NULL,
                `goalId` INTEGER,
                `note` TEXT,
                FOREIGN KEY(`goalId`) REFERENCES `lift_goals`(`id`) ON DELETE CASCADE
            )
        """.trimIndent())

            // Create table: lift_entries
            database.execSQL("""
            CREATE TABLE IF NOT EXISTS `lift_entries` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `sessionId` INTEGER NOT NULL,
                `liftName` TEXT NOT NULL,
                `liftType` TEXT NOT NULL,
                `weightKg` REAL NOT NULL,
                `reps` INTEGER NOT NULL,
                `sets` INTEGER NOT NULL,
                `timestamp` TEXT NOT NULL,
                FOREIGN KEY(`sessionId`) REFERENCES `lift_sessions`(`id`) ON DELETE CASCADE
            )
        """.trimIndent())
        }
    }
}