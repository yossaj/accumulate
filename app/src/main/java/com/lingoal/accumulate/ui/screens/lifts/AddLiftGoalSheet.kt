package com.lingoal.accumulate.ui.screens.lifts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingoal.accumulate.models.LiftEntry.LiftTypes
import com.lingoal.accumulate.ui.components.DropdownMenu
import com.lingoal.accumulate.ui.dimens.Dimens

@Composable
fun AddLiftGoalSheet(
    modifier: Modifier = Modifier,
    liftGoalId: Long? = null,
    viewModel: AddLiftGoalViewModel = hiltViewModel(),
    dismiss: () -> Unit,
) {

    LaunchedEffect(liftGoalId) {
        if (liftGoalId != null){
            viewModel.setLiftGoal(liftGoalId)
        }
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Exercises For This Session")
            Card {
                Column(
                    modifier.padding(Dimens.MarginMed),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
                ) {
                    OutlinedTextField(
                        value = state.liftName.orEmpty(),
                        onValueChange = viewModel::setLiftName,
                        label = { Text("Lift Name") },
                    )

                    DropdownMenu(
                        selectedLabel = state.liftType?.name.orEmpty(),
                        items = LiftTypes.entries,
                        onItemSelected = viewModel::setLiftType,
                        label = "Select Lift Type",
                        getItemLabel = { liftTypes -> liftTypes.name }
                    )

                    OutlinedTextField(
                        value = state.weight?.toString().orEmpty(),
                        onValueChange = viewModel::setWeight,
                        label = { Text("Weight") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Button(onClick = {
                        viewModel.addGoal()
                    }) {
                        Text("Add")
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.padding(Dimens.MarginMed),
                verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall)
            ) {
                items(state.lifts) { liftEntry ->
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimens.PaddingSmall)
                        ) {
                            Text(text = liftEntry.liftName)
                            Text(text = liftEntry.liftType.name)
                        }
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(Dimens.MarginMed)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            onClick = {
                viewModel.saveSessionAndGoals()
                dismiss.invoke()
            }) {
            Text(
                if(state.isUpdate){ "Add To Session" } else { "Start Session" }
            )
        }
    }
}