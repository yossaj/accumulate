package com.lingoal.accumulate.helpers

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.lingoal.accumulate.AppDatabase
import com.lingoal.accumulate.helpers.RoomHelper.MIGRATION_1_2
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomHelperTest {

    companion object {
        private const val TEST_DB = "migrationtest"
    }

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        listOf(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrate1To2_createsAllLiftTables() {
        var db = helper.createDatabase(TEST_DB, 1).apply {
            close()
        }

        // Migrate to version 2
        db = helper.runMigrationsAndValidate(
            TEST_DB,
            2,
            true,
            MIGRATION_1_2
        )

        val cursor = db.query("SELECT name FROM sqlite_master WHERE type='table'")
        val tableNames = mutableListOf<String>()
        while (cursor.moveToNext()) {
            tableNames.add(cursor.getString(0))
        }

        assertTrue(tableNames.contains("lift_goals"))
        assertTrue(tableNames.contains("lift_sessions"))
        assertTrue(tableNames.contains("lift_entries"))
    }

    @Test
    fun migrate1To2_preservesOldDataAndCreatesNewTables() {

        helper.createDatabase(TEST_DB, 1).apply {
            execSQL("""
                CREATE TABLE IF NOT EXISTS `goals` (
                    `id` TEXT NOT NULL PRIMARY KEY,
                    `name` TEXT NOT NULL,
                    `goalTime` REAL NOT NULL,
                    `totalAccumulatedTime` REAL NOT NULL,
                    `currentTimerStart` INTEGER,
                    `currentTimerRunning` INTEGER NOT NULL
                )
            """.trimIndent())

            // Insert sample data
            execSQL("""
                INSERT INTO goals
                (id, name, goalTime, totalAccumulatedTime, currentTimerStart, currentTimerRunning)
                VALUES ('ABC123', 'Old Goal', 5.0, 7200000, NULL, 0)
            """.trimIndent())

            close()
        }

        val migratedDb = helper.runMigrationsAndValidate(
            TEST_DB,
            2,
            true,
            MIGRATION_1_2
        )

        val cursor = migratedDb.query("SELECT name FROM sqlite_master WHERE type='table'")
        val tables = mutableListOf<String>()
        while (cursor.moveToNext()) {
            tables.add(cursor.getString(0))
        }

        assertTrue("lift_goals table not found", tables.contains("lift_goals"))
        assertTrue("lift_sessions table not found", tables.contains("lift_sessions"))
        assertTrue("lift_entries table not found", tables.contains("lift_entries"))

        val result = migratedDb.query("SELECT * FROM goals WHERE id = 'ABC123'")
        assertTrue("Old goal not found", result.moveToFirst())
        assertEquals("Old Goal", result.getString(result.getColumnIndexOrThrow("name")))
    }
}
