package com.lingoal.accumulate.models

import java.time.LocalDate

data class DailyLiftedTotal(
    val date: LocalDate,
    val total: Float
)