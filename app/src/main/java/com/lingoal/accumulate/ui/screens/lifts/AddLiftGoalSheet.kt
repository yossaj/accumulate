package com.lingoal.accumulate.ui.screens.lifts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.lingoal.accumulate.ui.dimens.Dimens

@Composable
fun AddLiftGoalSheet(
    modifier: Modifier = Modifier,
    viewModel: AddLiftGoalViewModel = hiltViewModel(),
    dismiss: () -> Unit,
) {
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
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Exercise Name") },
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Exercise Type") }
                )
                Button(onClick = {}) {
                    Text("Add")
                }
            }
        }

    }

}