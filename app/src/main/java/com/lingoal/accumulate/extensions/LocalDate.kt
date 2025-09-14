package com.lingoal.accumulate.extensions

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

val LocalDate.startOfWeek: LocalDate
    get() = this.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

val LocalDate.endofWeek: LocalDate
    get() = this.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

val LocalDate.startOfMonth: LocalDate
    get() = this.withDayOfMonth(1)

val LocalDate.endOfMonth: LocalDate
    get() = this.with(TemporalAdjusters.lastDayOfMonth())

fun LocalDate.toEndOfDayString(): LocalDateTime =
    this.atTime(LocalTime.MAX)