package com.lingoal.accumulate.ui.screens.dashboard

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lingoal.accumulate.ui.components.TotalTimeCard
import com.lingoal.accumulate.ui.theme.AccumulateTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
){

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val goals = state.goals

    when {
        goals.isNullOrEmpty() -> {
            Text(text = "Not Items")
        } else -> {
        LazyColumn(
            modifier = modifier
        )
        {
            items(goals){ goal ->
                TotalTimeCard(goal = goal)
            }
        }
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