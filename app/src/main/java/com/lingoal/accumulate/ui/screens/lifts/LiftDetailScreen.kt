package com.lingoal.accumulate.ui.screens.lifts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import java.text.DecimalFormat

@Composable
fun LiftDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: LiftDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val monthLiftsPointsData: List<Point> = state.data
    val xSteps = state.daysInMonth
    val liftTargetDataPoints: List<Point> = listOf(Point(1f,0f), Point(xSteps.toFloat(), 10000f))

    // ySteps
    val yLabelCount = 5
    val roundTo = 1000
    val maxRounded = ((state.maxValue  + roundTo - 1) / roundTo) * roundTo
    val stepSize = (maxRounded / yLabelCount)

    val xAxisData = AxisData.Builder()
        .axisStepSize(20.dp)
        .steps(xSteps)
        .labelData { i -> (i + 1).toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .labelAndAxisLinePadding(20.dp)
        .steps(yLabelCount)
        .labelData { i -> (i * stepSize).toString() }
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = monthLiftsPointsData,
                    LineStyle(lineType = LineType.Straight()),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                ),
                Line(
                    dataPoints = liftTargetDataPoints,
                    LineStyle(lineType = LineType.Straight()),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )

    if (monthLiftsPointsData.isNotEmpty()){
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = lineChartData
        )
    }
}

fun Float.formatToSinglePrecision(): String = DecimalFormat("#.#").format(this)
