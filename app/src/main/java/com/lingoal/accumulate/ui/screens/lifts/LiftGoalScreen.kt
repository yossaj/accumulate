package com.lingoal.accumulate.ui.screens.lifts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.AddCircle
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
import com.lingoal.accumulate.extensions.endofWeek
import com.lingoal.accumulate.extensions.startOfWeek
import com.lingoal.accumulate.models.LiftGoal
import com.lingoal.accumulate.ui.components.ProgressBar
import com.lingoal.accumulate.ui.dimens.Dimens
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LiftGoalScreen(
    modifier: Modifier = Modifier,
    viewModel: LiftGoalViewModel = hiltViewModel(),
    onGoalSet: (LiftGoal) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentDateState by viewModel.selectedDate.collectAsStateWithLifecycle()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm")

    LazyColumn(
        modifier = modifier
        .padding(Dimens.MarginMed)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall)
    ) {
        item {
            WeekNavigation(
                currentDate = currentDateState,
                onPreviousWeek = { viewModel.decrementWeek() },
                onNextWeek = { viewModel.incrementWeek()}
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
                                enabled = state.canSaveGoal,
                                onClick = { viewModel.addGoal() },
                                shape = RoundedCornerShape(Dimens.RoundingSmall)
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = Dimens.PaddingSmall),
                                    text = "Set", maxLines = 1)
                            }
                        }
                    } else {
                        state.goalWithSessionsAndLifts?.goal?.let { goal ->
                            onGoalSet.invoke(goal)
                            Text(text = state.cumulativeTotal.toString() +  " / " + goal.targetWeightKg.toString() + " Kg")
                            ProgressBar(progress = state.cumulativeTotal.toFloat() / goal.targetWeightKg.toFloat())

                        }
                    }
                }
            }
        }

        state.goalWithSessionsAndLifts?.sessionsWithLifts?.let { sessionWithLifts ->

            sessionWithLifts.forEach { sessionWithLifts ->
                item{
                    Text(text = sessionWithLifts.session.date.format(formatter))
                }

                items(sessionWithLifts.lifts) { liftEntry ->
                    Card {
                        Column(
                            modifier = Modifier
                                .padding(Dimens.MarginSmall)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(text = liftEntry.liftName)
                                Text(text = liftEntry.liftType.toString())
                            }
                            Text(text = "Reps: " + liftEntry.reps.toString())
                            Row( verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Sets: " + liftEntry.sets.toString())
                                IconButton(onClick = { viewModel.incrementSet(liftEntry) }) {
                                    Icon(
                                        imageVector = Icons.Rounded.AddCircle,
                                        contentDescription = "Add Goal")
                                }
                            }
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
            text = "${currentDate.startOfWeek} – ${currentDate.endofWeek}",
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