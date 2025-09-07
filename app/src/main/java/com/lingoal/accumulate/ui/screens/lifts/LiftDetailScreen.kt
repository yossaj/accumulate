package com.lingoal.accumulate.ui.screens.lifts

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
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
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.lingoal.accumulate.ui.dimens.Dimens

@Composable
fun LiftDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: LiftDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {

        val screenWidth = maxWidth
        val xStepSize = if (state.xSteps > 0) {
            screenWidth / state.xSteps
        } else {
            0.dp
        }

        val data = state.cumulativePoints.map { (x, y) -> Point(x, y) }
        val targetLine = state.liftTargetPoints.map { (x, y) -> Point(x, y) }

        val xAxisData = AxisData.Builder()
            .axisStepSize(xStepSize)
            .steps(state.xSteps)
            .labelData { i ->
                if (i % (state.xSteps / 12).coerceAtLeast(1) == 0) (i + 1).toString()  else ""
            }
            .labelAndAxisLinePadding(15.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .labelAndAxisLinePadding(20.dp)
            .steps(state.yLabelCount)
            .labelData { i -> (i * state.yStepSize).toString() }
            .build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = data,
                        LineStyle(lineType = LineType.Straight()),
                        IntersectionPoint(),
                        SelectionHighlightPoint(),
                        ShadowUnderLine(),
                        SelectionHighlightPopUp()
                    ),
                    Line(
                        dataPoints = targetLine,
                        LineStyle(
                            lineType = LineType.Straight(),
                            color = Color.Red
                        ),
                        IntersectionPoint(
                            color = Color.Transparent
                        ),
                        SelectionHighlightPoint(),
                        ShadowUnderLine(color = Color.Transparent),
                        SelectionHighlightPopUp()
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(),
            backgroundColor = Color.Transparent
        )

        val donutChartData = PieChartData(
            slices = listOf(
                PieChartData.Slice("Legs", state.legTotal, Color(0xFF20BF55)),
                PieChartData.Slice("Push", state.pushTotal,  Color(0xFFEC9F05)),
                PieChartData.Slice("Pull", state.pullTotal, Color(0xFFF53844))
            ),
            plotType = PlotType.Donut
        )

        val donutChartConfig = PieChartConfig(
            strokeWidth = 80f,
            activeSliceAlpha = .9f,
            isAnimationEnable = true,
            backgroundColor = Color.Transparent
        )

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePeriodSegmentedButton(
                modifier = Modifier.fillMaxWidth(),
                viewModel::setSelectedTimePeriod
            )

            if (data.isNotEmpty()){
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    lineChartData = lineChartData
                )
            }

            DonutPieChart(
                modifier = Modifier.padding(Dimens.MarginMed),
                donutChartData,
                donutChartConfig
            )
        }

    }
}

@Composable
fun TimePeriodSegmentedButton(
    modifier: Modifier = Modifier,
    onPeriodSelected: (LiftDetailUIState.TimePeriod) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = LiftDetailUIState.TimePeriod.entries.toTypedArray()

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, timePeriod ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    selectedIndex = index
                    onPeriodSelected.invoke(timePeriod)
                          },
                selected = index == selectedIndex,
                label = { Text(timePeriod.text) }
            )
        }
    }
}
