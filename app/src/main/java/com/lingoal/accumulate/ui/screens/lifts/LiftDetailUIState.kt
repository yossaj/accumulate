package com.lingoal.accumulate.ui.screens.lifts

import co.yml.charts.common.model.Point
import com.lingoal.accumulate.models.DailyLiftedTotal
import java.time.LocalDate
import java.time.YearMonth

data class LiftDetailUIState(
    val currentGoal: Int? = null,

    val liftTotals: List<DailyLiftedTotal> = emptyList()
) {
    val maxValue = liftTotals.maxOfOrNull { it.total }?.toInt() ?: 0
    private val yearMonth = YearMonth.from(liftTotals.firstOrNull()?.date ?: LocalDate.now())
    val daysInMonth = yearMonth.lengthOfMonth()

    private var cumulative = 0f
    val data: List<Point> = liftTotals
        .map { entry ->
            cumulative += entry.total
            Point(entry.date.dayOfMonth.toFloat(), cumulative)
        }}