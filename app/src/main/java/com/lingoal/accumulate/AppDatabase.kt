package com.lingoal.accumulate

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.lingoal.accumulate.models.Goal
import com.lingoal.accumulate.models.GoalDao
import com.lingoal.accumulate.models.LiftEntry
import com.lingoal.accumulate.models.LiftEntryDao
import com.lingoal.accumulate.models.LiftGoal
import com.lingoal.accumulate.models.LiftGoal.PeriodType
import com.lingoal.accumulate.models.LiftGoalDao
import com.lingoal.accumulate.models.LiftSession
import com.lingoal.accumulate.models.LiftSessionDao
import java.time.LocalDate
import java.time.LocalDateTime

@Database(entities = [Goal::class, LiftGoal::class, LiftSession::class, LiftEntry::class], version = AppDatabase.VERSION)
@TypeConverters(DateConverters::class, EnumConverters::class)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        const val VERSION = 2
    }

    abstract fun goalDao(): GoalDao
    abstract fun liftGoalDao(): LiftGoalDao
    abstract fun liftSessionDao(): LiftSessionDao
    abstract fun liftEntryDao(): LiftEntryDao
}

class DateConverters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(date: String?): LocalDate? = date?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.toString()

    @TypeConverter
    fun toLocalDateTime(dateTime: String?): LocalDateTime? = dateTime?.let { LocalDateTime.parse(it) }
}

class EnumConverters {
    @TypeConverter
    fun fromPeriodType(value: PeriodType) = value.name

    @TypeConverter
    fun toPeriodType(value: String) = enumValueOf<PeriodType>(value)
}