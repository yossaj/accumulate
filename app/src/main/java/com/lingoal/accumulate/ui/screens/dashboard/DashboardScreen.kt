package com.lingoal.accumulate.ui.screens.dashboard

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingoal.accumulate.ui.components.TotalTimeCard
import com.lingoal.accumulate.ui.dimens.Dimens
import com.lingoal.accumulate.ui.theme.AccumulateTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
){

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var openAddTimeSheet by rememberSaveable { mutableStateOf(false) }
    val addTimeSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    when {
        state.goals.isEmpty() -> {
            Text(text = "Not Items")
        }

        else -> {
            LazyColumn(
                modifier = modifier
            )
            {
                items(state.goals) { goal ->
                    TotalTimeCard(
                        goal = goal,
                        startTimer = { viewModel.startTimer(goal.id) },
                        stopTimer = { viewModel.stopTimer(goal.id) },
                        addTime = {
                            viewModel.setGoal(goal)
                            openAddTimeSheet = !openAddTimeSheet
                        }
                    )
                }
            }
        }
    }

    if (openAddTimeSheet) {
        ModalBottomSheet(
            onDismissRequest = { openAddTimeSheet = false },
            sheetState = addTimeSheetState) {
            Column(
                modifier = modifier
                    .padding(Dimens.MarginMed)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = state.selectedGoal?.name ?: "")

                OutlinedTextFieldDefaults.DecorationBox(
                    enabled = true,
                    interactionSource = remember { MutableInteractionSource() },
                    visualTransformation = VisualTransformation.None,
                    singleLine = true,
                    innerTextField = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            TextField(
                                modifier = Modifier.weight(0.5f),
                                value = state.hours ?: "",
                                onValueChange = { viewModel.setHours(it) },
                                placeholder = {
                                    Text(text = "Hours")
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Number
                                )
                            )
                            Text(
                                text = ":",
                                style = MaterialTheme.typography.titleLarge,
                            )
                            TextField(
                                modifier = Modifier.weight(0.5f),
                                value = state.minutes ?: "",
                                onValueChange = { viewModel.setMinutes(it) },
                                placeholder = {
                                    Text(text = "Minutes")
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Number
                                )
                            )
                        }
                    },
                    value = " ",
                    label = {
                        Text(text = "Add Time")
                    }
                )

                Button(
                    enabled = state.canAddTime,
                    onClick = {
                        viewModel.addTime()
                        openAddTimeSheet = false
                    }) {
                    Text(text = "Add")
                }
            }

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Preview
@Composable
private fun DashboardPreview() {
    AccumulateTheme {
        DashboardScreen()
    }
}