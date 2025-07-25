package com.lingoal.accumulate.extensions

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

val LocalDate.startOfWeek
    get() = this.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

val LocalDate.endofWeek
    get() = this.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))