package com.lingoal.accumulate.ui.screens.lifts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingoal.accumulate.ui.dimens.Dimens
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun LiftGoalScreen(
    modifier: Modifier = Modifier,
    viewModel: LiftGoalViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
        .padding(Dimens.MarginMed)
        .fillMaxWidth(),
    ) {
        item {
            WeekNavigation(
                currentDate = LocalDate.now(),
                onPreviousWeek = {},
                onNextWeek = {}
            )
        }

        item {
            Card {
                Column(
                    modifier = Modifier.padding(Dimens.MarginMed).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.goalWithSessionsAndLifts?.goal == null){
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.weight(0.75f),
                                value = state.liftGoal.orEmpty(),
                                onValueChange = viewModel::setLiftGoal,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                placeholder = { Text("Add Weekly Goal") },
                                singleLine = false,
                            )
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(Dimens.RoundingSmall)
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = Dimens.PaddingSmall),
                                    text = "Set", maxLines = 1)
                            }
                        }
                    } else {
                        state.goalWithSessionsAndLifts?.goal?.let { goal ->
                            Text(text = goal.targetWeightKg.toString() + " Kg")
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun WeekNavigation(
    currentDate: LocalDate,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    val weekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekEnd = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousWeek) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous Week"
            )
        }

        Text(
            text = "$weekStart – $weekEnd",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = Dimens.MarginMed)
        )

        IconButton(onClick = onNextWeek) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next Week"
            )
        }
    }
}