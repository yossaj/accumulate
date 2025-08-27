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
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
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
import com.lingoal.accumulate.models.cumulativeWeight
import com.lingoal.accumulate.ui.components.ProgressBar
import com.lingoal.accumulate.ui.dimens.Dimens
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LiftGoalScreen(
    modifier: Modifier = Modifier,
    viewModel: LiftGoalViewModel = hiltViewModel(),
    onGoalSet: (Long) -> Unit,
    onOpenDetails: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentDateState by viewModel.selectedDate.collectAsStateWithLifecycle()
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yy HH:mm")
    val decimalFormatter = DecimalFormat("#.## Kg")

    LazyColumn(
        modifier = modifier
            .padding(Dimens.MarginMed)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall)
    ) {
        item {
            Button(onClick = {
                onOpenDetails.invoke()
            }) {
                Text("Progress")
            }
        }
        item {
            WeekNavigation(
                currentDate = currentDateState,
                onPreviousWeek = { viewModel.decrementWeek() },
                onNextWeek = { viewModel.incrementWeek() }
            )
        }

        item {
            Card {
                Column(
                    modifier = Modifier
                        .padding(Dimens.MarginMed)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.liftGoal == null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.weight(0.75f),
                                value = state.liftGoalId.orEmpty(),
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
                                    text = "Set", maxLines = 1
                                )
                            }
                        }
                    } else {
                        state.liftGoal?.let { goal ->
                            onGoalSet.invoke(goal.id)
                            Text(text = state.cumulativeTotal.toString() + " / " + goal.targetWeightKg.toString() + " Kg")
                            ProgressBar(progress = state.cumulativeTotal.toFloat() / goal.targetWeightKg.toFloat())
                        }
                    }
                }
            }
        }


        state.sessionsWithLifts.forEach { sessionWithLifts ->
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = sessionWithLifts.session.date.format(dateTimeFormatter))
                    Text(text = decimalFormatter.format(sessionWithLifts.cumulativeWeight))
                }

            }

            items(sessionWithLifts.lifts) { liftEntry ->
                Card {
                    Row(
                        modifier = Modifier
                            .padding(Dimens.MarginSmall)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = liftEntry.liftType.toString(),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = liftEntry.liftName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Row {
                                Text(
                                    text = decimalFormatter.format(liftEntry.weightKg),
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(" • ")
                                Text(
                                    text = "${liftEntry.reps} reps",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }

                        ElevatedCard {
                            Row(
                                modifier = Modifier.padding(start = Dimens.PaddingMed),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${liftEntry.sets}",
                                        style = MaterialTheme.typography.displaySmall
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Sets")
                                    }
                                }

                                Column {
                                    IconButton(onClick = { viewModel.incrementSet(liftEntry) }) {
                                        Icon(
                                            imageVector = Icons.Rounded.KeyboardArrowUp,
                                            contentDescription = "Add Set"
                                        )
                                    }

                                    IconButton(onClick = { viewModel.decrementSet(liftEntry) }) {
                                        Icon(
                                            imageVector = Icons.Rounded.KeyboardArrowDown,
                                            contentDescription = "Remove Set"
                                        )
                                    }
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
    val formatter = DateTimeFormatter.ofPattern("dd MMM")

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousWeek) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous Week"
            )
        }

        Text(
            text ="${currentDate.startOfWeek.format(formatter)} – ${currentDate.endofWeek.format(formatter)}",
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