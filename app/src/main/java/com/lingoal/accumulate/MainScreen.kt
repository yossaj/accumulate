package com.lingoal.accumulate

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lingoal.accumulate.ui.screens.dashboard.DashboardScreen
import com.lingoal.accumulate.ui.screens.goal.AddGoalSheet
import com.lingoal.accumulate.ui.screens.goal.GoalDetailScreen
import com.lingoal.accumulate.ui.theme.AccumulateTheme

enum class Screens(@StringRes val title: Int){
    Dashboard(R.string.app_name),
    GoalDetail(R.string.goal_detail)
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
){
    val backStackEntry by navController.currentBackStackEntryAsState()

    val screenName = backStackEntry?.destination?.route?.split("/", "?")?.first()

    val currentScreen = Screens.valueOf(
        screenName ?: "Dashboard"
    )

    val dynamicScreenTitle = remember { mutableStateOf<String?>(null) }

    var openAddGoalSheet by rememberSaveable { mutableStateOf(false) }
    val addGoalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(
            currentScreen = currentScreen,
            dynamicScreenTitle = dynamicScreenTitle,
            navigateUp = { navController.navigateUp() },
            addGoal = { openAddGoalSheet = !openAddGoalSheet }
        ) }
    ) { innerPadding ->


        NavHost(
            navController = navController,
            startDestination = Screens.Dashboard.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Screens.Dashboard.name
            ) {
                DashboardScreen(
                    addInitialGoal = { openAddGoalSheet = !openAddGoalSheet },
                    openGoal = { goalId, goalName ->
                        dynamicScreenTitle.value = goalName
                        navController.navigate(Screens.GoalDetail.name + "?goalId=$goalId")
                    }
                )
            }

            composable(
                route = Screens.GoalDetail.name + "?goalId={goalId}",
                arguments = listOf(
                    navArgument("goalId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) {
                GoalDetailScreen()
            }
        }

        if(openAddGoalSheet){
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(0.5f),
                onDismissRequest = { openAddGoalSheet = false },
                sheetState = addGoalSheetState
            ) {
                AddGoalSheet( dismiss = { openAddGoalSheet = false })
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }



    }
}

@Composable
fun AppBar(
    currentScreen: Screens,
    dynamicScreenTitle: State<String?>,
    addGoal: () -> Unit,
    navigateUp: () -> Unit,
){
    TopAppBar(
        title = {
            Text(
                text = getTitle(currentScreen, dynamicScreenTitle),
                maxLines = 1,
            )
        },
        navigationIcon = {
            if (currentScreen != Screens.Dashboard) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back navigation"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { addGoal.invoke() }) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Add Goal")
            }
        }
    )
}

@Composable
private fun getTitle(
    currentScreen: Screens,
    dynamicScreenTitle: State<String?>
) = if (currentScreen != Screens.Dashboard) {
    dynamicScreenTitle.value.takeIf { !it.isNullOrEmpty() }
        ?: stringResource(id = currentScreen.title)

} else {
    stringResource(id = currentScreen.title)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AccumulateTheme {
        MainScreen()
    }
}