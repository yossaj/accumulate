package com.lingoal.accumulate

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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

enum class RootDestination(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector,
    val contentDescription: String
) {
    TIME("time", R.string.time, Icons.Default.Timer, "Time"),
    WEIGHT("weight", R.string.weight, Icons.Default.Sports, "Weight"),
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
){

    val backStackEntry by navController.currentBackStackEntryAsState()

    val screenName = backStackEntry?.destination?.route?.split("/", "?")?.first()

    val currentTab = RootDestination.valueOf(
        screenName?.uppercase() ?: "TIME"
    )

    var openAddGoalSheet by rememberSaveable { mutableStateOf(false) }
    val addGoalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val startDestination = RootDestination.TIME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
            currentTab = currentTab,
            navigateUp = { navController.navigateUp() },
            addGoal = { openAddGoalSheet = !openAddGoalSheet }
        ) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            PrimaryTabRow(selectedTabIndex = selectedDestination) {
                RootDestination.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        text = {
                            Text(
                                text = stringResource(destination.title),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }

            NavHost(
                navController,
                startDestination = startDestination.route
            ) {
                RootDestination.entries.forEach { destination ->
                    composable(destination.route) {
                        when (destination) {
                            RootDestination.TIME -> TimeAccumulateScreen(
                                onAddGoal =  { openAddGoalSheet = !openAddGoalSheet }
                            )
                            RootDestination.WEIGHT -> WeightAccumulateScreen()
                        }
                    }
                }
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
private fun TimeAccumulateScreen(
    modifier: Modifier = Modifier,
    timeNavController: NavHostController = rememberNavController(),
    onAddGoal: () -> Unit,
){
    NavHost(
        navController = timeNavController,
        startDestination = Screens.Dashboard.name,
        modifier = modifier
    ) {
        composable(
            route = Screens.Dashboard.name
        ) {
            DashboardScreen(
                addInitialGoal = onAddGoal,
                openGoal = { goalId, goalName ->
                    timeNavController.navigate(Screens.GoalDetail.name + "?goalId=$goalId")
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
}

@Composable
fun WeightAccumulateScreen(
    navController: NavHostController = rememberNavController(),
){

}

@Composable
fun AppBar(
    currentTab: RootDestination,
    addGoal: () -> Unit,
    navigateUp: () -> Unit,
){
    TopAppBar(
        title = {
            Text(
                text = stringResource(currentTab.title),
                maxLines = 1,
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back navigation"
                )
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AccumulateTheme {
        MainScreen()
    }
}