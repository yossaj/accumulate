package com.lingoal.accumulate.ui.components

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.lingoal.accumulate.models.Goal
import com.lingoal.accumulate.ui.dimens.Dimens
import com.lingoal.accumulate.ui.theme.AccumulateTheme
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@Composable
fun TotalTimeCard(
    modifier: Modifier = Modifier,
    goal: Goal,
    startTimer: () -> Unit,
    stopTimer: () -> Unit,
    addTime: () -> Unit,
){

    var currentTimeElapsed by remember { mutableStateOf("00:00:00") }

    LaunchedEffect(goal.currentTimerStart) {
        goal.currentTimerStart?.let { startTime ->
            while (goal.currentTimerRunning) {
                val timeSpan = SystemClock.elapsedRealtime() - startTime
                val hours = (timeSpan / 3600000) % 24
                val minutes = (timeSpan / 60000) % 60
                val seconds = (timeSpan / 1000) % 60

                currentTimeElapsed = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)

                delay(1.seconds)
            }
        }
    }

    Card(
        modifier.padding(Dimens.MarginSmall)
    ) {
        Column(
            modifier
                .padding(Dimens.PaddingMed),
            verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = goal.name)

                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (goal.currentTimerRunning){
                        Text(text = currentTimeElapsed)
                    }

                    if (goal.currentTimerRunning){
                        IconButton(onClick = { stopTimer.invoke() }) {
                            Icon(
                                imageVector = Icons.Rounded.Stop,
                                contentDescription = "Start Recording")
                        }
                    } else {
                        IconButton(onClick = { startTimer.invoke() }) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Start Recording")
                        }
                    }


                    IconButton(onClick = { addTime.invoke()}) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Start Recording")
                    }
                }


            }

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = {
                    goal.progress
                           },
                color = Color.Magenta,
                trackColor = Color.LightGray
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatElapsedTime(goal.totalAccumulatedTime))
                Text(text = "Goal: ${goal.goalTime.toInt()}hr")
            }
        }

    }
}

fun formatElapsedTime(milliseconds: Float): String {
    val totalMinutes = (milliseconds / 1000 / 60).toInt()
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return "${hours}hr ${minutes} min"
}

@Preview
@Composable
private fun TotalTimeCardPreview() {
    AccumulateTheme {
        TotalTimeCard(
            goal = Goal(
            name = "Chinese Listening",
            goalTime = 100f,
            totalAccumulatedTime = 10f,
        ),
            startTimer = {},
            stopTimer = {},
            addTime = {}
        )
    }
}