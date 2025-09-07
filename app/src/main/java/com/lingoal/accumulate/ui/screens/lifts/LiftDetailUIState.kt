package com.lingoal.accumulate.ui.screens.lifts

import com.lingoal.accumulate.models.DailyLiftedTotal
import java.time.LocalDate
import java.time.YearMonth

data class LiftDetailUIState(
    val legTotal: Float = 0f,
    val pullTotal: Float = 0f,
    val pushTotal: Float = 0f,
    val targetWeight: Int? = null,
    val selectedPeriod: TimePeriod = TimePeriod.Weekly,
    val liftTotals: List<DailyLiftedTotal> = emptyList()
) {
    private val maxValue = liftTotals.maxOfOrNull { it.total }?.toInt() ?: 0

    val cumulativePoints: List<Pair<Float, Float>> by lazy {
        var cumulative = 0f

        val grouped = when (selectedPeriod) {
            TimePeriod.Weekly -> liftTotals.groupBy { it.date.dayOfWeek.value }
            TimePeriod.Monthly -> liftTotals.groupBy { it.date.dayOfMonth }
            TimePeriod.Yearly -> liftTotals.groupBy { it.date.monthValue }
        }

        listOf(1f to 0f) + grouped
            .toSortedMap()
            .map { (x, entries) ->
                cumulative += entries.map { it.total }.sum()
                x.toFloat() to cumulative
            }
    }

    val liftTargetPoints: List<Pair<Float, Float>> by lazy {
        val ref = targetWeight?.toFloat() ?: 10000f
        listOf(1f to ref, xSteps.toFloat() to ref)
    }

    private val yearMonth = YearMonth.from(liftTotals.firstOrNull()?.date ?: LocalDate.now())

    val xSteps: Int = when(selectedPeriod){
        TimePeriod.Weekly ->  7
        TimePeriod.Monthly -> yearMonth.lengthOfMonth()
        TimePeriod.Yearly -> 12
    }

    val yLabelCount: Int = 5
    private val roundTo: Int = 1000
    private val maxRounded: Int = ((maxValue + roundTo - 1) / roundTo) * roundTo
    val yStepSize: Int = maxRounded / yLabelCount

    enum class TimePeriod(
        val text: String
    ){
        Weekly("7d"),
        Monthly("4w"),
        Yearly("1y")
    }
}