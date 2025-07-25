package com.lingoal.accumulate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "lift_goals")
data class LiftGoal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetWeightKg: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val periodType: PeriodType
){

    enum class PeriodType() {
        Weekly,
        Monthly,
    }
}