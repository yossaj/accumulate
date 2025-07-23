package com.lingoal.accumulate.ui.screens.time.goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingoal.accumulate.ui.components.ProgressBar
import com.lingoal.accumulate.ui.dimens.Dimens

@Composable
fun GoalDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: GoalDetailViewModel = hiltViewModel(),
){
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    state.goal?.let { goal ->
        Column(
            modifier = Modifier.padding(Dimens.MarginMed),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall)
        ) {

            Card {
                Column(
                    modifier = Modifier.padding(Dimens.PaddingMed),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall)
                ){
                    Text(
                        text = "${goal.goalTime.toInt()} Hours",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    ProgressBar(progress = goal.progress)

                }
            }

        }
    }

}