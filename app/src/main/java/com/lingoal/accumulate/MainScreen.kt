package com.lingoal.accumulate

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lingoal.accumulate.ui.screens.dashboard.DashboardScreen
import com.lingoal.accumulate.ui.screens.goal.AddGoalSheet
import com.lingoal.accumulate.ui.theme.AccumulateTheme

enum class Screens(@StringRes val title: Int){
    Dashboard(R.string.dashboard)
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
){
    var openAddGoadSheet by rememberSaveable { mutableStateOf(false) }
    val addGoalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(
            addGoal = { openAddGoadSheet = !openAddGoadSheet }
        ) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Dashboard.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(
                route = Screens.Dashboard.name
            ){
                DashboardScreen()
            }

        }

        if(openAddGoadSheet){
            ModalBottomSheet(
                onDismissRequest = { openAddGoadSheet = false },
                sheetState = addGoalSheetState
            ) {
                AddGoalSheet( dismiss = { openAddGoadSheet = false })
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }

    }
}

@Composable
fun AppBar(
    addGoal: () -> Unit
){
    TopAppBar(
        title = { Text(text = "Accumulate") },
        actions = {
            IconButton(onClick = { addGoal.invoke() }) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Add Goal")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AccumulateTheme {
        MainScreen()
    }
}