package com.lingoal.accumulate.ui.screens.goal

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GoalDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: GoalDetailViewModel = hiltViewModel(),
){
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    
    Column {
        Text(text = state.goal?.name ?: "Not Found")
    }
}