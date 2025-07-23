package com.lingoal.accumulate.ui.screens.time.goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingoal.accumulate.ui.dimens.Dimens

@Composable
fun AddGoalSheet(
    modifier: Modifier = Modifier,
    viewModel: AddGoalViewModel = hiltViewModel(),
    dismiss: () -> Unit,
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(Dimens.MarginSmall)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.name ?: "",
            onValueChange = { viewModel.updateName(it) },
            placeholder = {
                Text(text = "Activity")
            })
        OutlinedTextField(
            value = state.cumulativeHours ?: "",
            onValueChange = { viewModel.updateCumulativeHours(it) },
            placeholder = {
                Text(text = "Cumulative Hours")
            }
        )
        Button(
            enabled = state.canSave,
            onClick = {
                viewModel.addGoal()
                dismiss.invoke()
            }) {
            Text(text = "Add")
        }
    }
}

@Preview
@Composable
private fun PreviewAddGoal() {
    AddGoalSheet(dismiss = {})

}