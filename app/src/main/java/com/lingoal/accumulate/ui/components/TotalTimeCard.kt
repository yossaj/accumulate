package com.lingoal.accumulate.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.lingoal.accumulate.models.Goal
import com.lingoal.accumulate.ui.dimens.Dimens
import com.lingoal.accumulate.ui.theme.AccumulateTheme

@Composable
fun TotalTimeCard(
    modifier: Modifier = Modifier,
    goal: Goal
){
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
//                    Text(text = "Elapsed Time")

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Start Recording")
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Start Recording")
                    }
                }


            }

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { goal.progress },
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
        TotalTimeCard(goal = Goal(
            name = "Chinese Listening",
            goalTime = 100f,
            totalAccumulatedTime = 10f
        ))
    }
}