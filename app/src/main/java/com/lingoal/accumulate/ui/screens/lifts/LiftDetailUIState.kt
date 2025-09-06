package com.lingoal.accumulate.ui.screens.lifts

import co.yml.charts.common.model.Point
import com.lingoal.accumulate.models.DailyLiftedTotal
import java.time.LocalDate
import java.time.YearMonth

data class LiftDetailUIState(
    val targetWeight: Int? = null,
    val selectedPeriod: TimePeriod = TimePeriod.Weekly,

    val liftTotals: List<DailyLiftedTotal> = emptyList()
) {
    val maxValue = liftTotals.maxOfOrNull { it.total }?.toInt() ?: 0
    private val yearMonth = YearMonth.from(liftTotals.firstOrNull()?.date ?: LocalDate.now())
    val daysInMonth = yearMonth.lengthOfMonth()

    private var cumulative = 0f
    val data: List<Point>
        get() {
            return listOf(Point(1f, 0f)) + liftTotals.map { entry ->
                cumulative += entry.total

                val x = when (selectedPeriod) {
                    TimePeriod.Weekly -> entry.date.dayOfWeek.value.toFloat()
                    TimePeriod.Monthly -> entry.date.dayOfMonth.toFloat()
                    TimePeriod.Yearly -> entry.date.monthValue.toFloat()
                }

                Point(x, cumulative)
            }
        }

    enum class TimePeriod(
        val text: String
    ){
        Weekly("7d"),
        Monthly("4w"),
        Yearly("1y")
    }
}