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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.lingoal.accumulate.ui.screens.lifts.AddLiftGoalSheet
import com.lingoal.accumulate.ui.screens.lifts.LiftGoalScreen
import com.lingoal.accumulate.ui.screens.time.dashboard.DashboardScreen
import com.lingoal.accumulate.ui.screens.time.goal.AddTimeGoalSheet
import com.lingoal.accumulate.ui.screens.time.goal.GoalDetailScreen
import com.lingoal.accumulate.ui.theme.AccumulateTheme

enum class TimeScreens(@StringRes val title: Int){
    Dashboard(R.string.app_name),
    Detail(R.string.goal_detail)
}

enum class LiftScreens(@StringRes val title: Int){
    Dashboard(R.string.app_name),
    Detail(R.string.goal_detail)
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

//    val currentTab = RootDestination.valueOf(
//        screenName?.uppercase() ?: "TIME"
//    )

    var openAddGoalSheet by rememberSaveable { mutableStateOf(false) }
    val addGoalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val startDestination = RootDestination.TIME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    var currentLiftGoalId: Long? by rememberSaveable { mutableStateOf(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = screenName,
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
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                            selectedDestination = index
                        },
                        text = {
                            Text(
                                text = stringResource(destination.title),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        }
                    )
                }
            }

            NavHost(
                navController = navController,
                startDestination = RootDestination.TIME.route
            ) {
                timeGraph(navController, onAddGoal = { openAddGoalSheet = true })
                weightGraph(navController, onGoalSet = { liftGoal ->   currentLiftGoalId = liftGoal })
            }
        }

        if(openAddGoalSheet){
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(
                    when(selectedDestination){
                        0 -> 0.5f
                        1 -> 0.95f
                        else -> 0.5f
                    }
                ),
                onDismissRequest = { openAddGoalSheet = false },
                sheetState = addGoalSheetState
            ) {
                key(selectedDestination) {
                    when(selectedDestination){
                        0 -> AddTimeGoalSheet( dismiss = { openAddGoalSheet = false })
                        1 -> AddLiftGoalSheet(
                            liftGoalId = currentLiftGoalId,
                            sheetState = addGoalSheetState,
                            dismiss = { openAddGoalSheet = false },
                        )
                    }
                }
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

fun NavGraphBuilder.timeGraph(navController: NavHostController, onAddGoal: () -> Unit) {
    navigation(startDestination = TimeScreens.Dashboard.name, route = RootDestination.TIME.route) {
        composable(TimeScreens.Dashboard.name) {
            DashboardScreen(
                addInitialGoal = onAddGoal,
                openGoal = { goalId, goalName ->
                    navController.navigate(TimeScreens.Detail.name + "?goalId=$goalId")
                }
            )
        }
        composable(
            route = TimeScreens.Detail.name + "?goalId={goalId}",
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

fun NavGraphBuilder.weightGraph(navController: NavHostController, onGoalSet: (Long) -> Unit) {
    navigation(startDestination = LiftScreens.Dashboard.name, route = RootDestination.WEIGHT.route) {
        composable(LiftScreens.Dashboard.name) {
            LiftGoalScreen(
                onGoalSet = onGoalSet
            )
        }
    }
}


@Composable
fun AppBar(
    title: String?,
    addGoal: () -> Unit,
    navigateUp: () -> Unit,
){
    TopAppBar(
        title = {
            Text(
                text = title.orEmpty(),
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