package com.lingoal.accumulate.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingoal.accumulate.ui.components.TotalTimeCard
import com.lingoal.accumulate.ui.dimens.Dimens
import com.lingoal.accumulate.ui.theme.AccumulateTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
    addInitialGoal: () -> Unit,
    openGoal: (String, String) -> Unit
){
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var openAddTimeSheet by rememberSaveable { mutableStateOf(false) }
    val addTimeSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    when {
        state.goals.isEmpty() -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.MarginMed),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall, Alignment.CenterVertically)
            ) {
                Text(
                    text = "No Goals Added",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                    )
                Text(
                    text = "Create a new goal to track your accumulated hours towards that goal.",
                    textAlign = TextAlign.Center
                    )
                Button(onClick = {
                    addInitialGoal.invoke()
                }) {
                    Text(text = "Get Started")
                }
            }

        }

        else -> {
            LazyColumn(modifier = modifier)
            {
                items(state.goals) { goal ->
                    TotalTimeCard(
                        modifier = Modifier.clickable {
                            openGoal.invoke(goal.id, goal.name)
                        },
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

                Row {
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


                    IconButton(
                        enabled = state.canAddTime,
                        onClick = {
                            viewModel.saveAddedTime()
                            openAddTimeSheet = false
                        }
                    ) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add From Video link")
                    }
                }


                Button(
                    enabled = state.canAddTime,
                    onClick = {
                        viewModel.saveAddedTime()
                        openAddTimeSheet = false
                    }) {
                    Text(text = "Add")
                }
                
                Spacer(modifier = Modifier.padding(Dimens.MarginMed))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(0.2f),
                        textAlign = TextAlign.Center,
                        text = state.videoDurationString ?: "00:00")
                    OutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        singleLine = true,
                        value = state.videoUrlString ?: "",
                        onValueChange = { viewModel.setVideoUrl(it)},
                        placeholder = { Text(text = "Paste a video link")}
                    )

                    IconButton(
                        enabled = state.canSaveVideoDuration,
                        onClick = {
                            viewModel.saveVideoDuration()
                            openAddTimeSheet = false
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add From Video link"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier
                .navigationBarsPadding()
                .padding(Dimens.MarginMed)
            )
        }
    }
}

@Preview
@Composable
private fun DashboardPreview() {
    AccumulateTheme {
        DashboardScreen(
            addInitialGoal = {},
            openGoal = {  param1, param2 -> }
        )
    }
}