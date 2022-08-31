package org.feature.fox.coffee_counter.ui.transaction

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CustomButton
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.theme.Cinnabar
import org.feature.fox.coffee_counter.ui.theme.MediumSeaGreen
import org.feature.fox.coffee_counter.ui.theme.MoonYellow
import org.feature.fox.coffee_counter.ui.theme.SummerSky
import org.feature.fox.coffee_counter.util.DateTimeFormatter
import org.feature.fox.coffee_counter.util.UIText
import java.text.SimpleDateFormat
import java.util.*

private val rowTextFontSize: TextUnit = 18.sp

/**
 * Preview for the HistoryView.
 */
@Preview(showSystemUi = true)
@Composable
fun HistoryViewPreview(
) {
    HistoryView(viewModel = TransactionViewModelPreview())
}

/**
 * Composable for the HistoryView.
 * TODO update orders (Issue)
 * @param viewModel the transaction ViewModel.
 */
@Composable
fun HistoryView(
    viewModel: ITransactionViewModel,
) {
    QRCodeDialog(viewModel)
    Column {
        MoneyAppBar(Pair(stringResource(R.string.history_title), viewModel.balance))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(5.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QRCodeButton(viewModel)
        }
        PieChartBoughtItems(viewModel)
        LineChartBalance(viewModel)
        TransactionContainer(viewModel)
    }
}

/**
 * Holds all the transactions.
 * @param viewModel the transaction ViewModel.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TransactionContainer(viewModel: ITransactionViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(5.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        coroutineScope.launch {
            viewModel.refreshTransactions()
            viewModel.getTotalBalance()
        }
        if (viewModel.transactions.isEmpty()) Text(
            stringResource(id = R.string.no_data),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.LightGray
        )


        viewModel.transactions.forEach { transaction ->
            if (transaction.type == "funding") {
                TransactionRow(
                    type = "Funding",
                    date = SimpleDateFormat(BuildConfig.DATE_PATTERN, Locale.GERMAN).format(
                        Date(
                            transaction.timestamp
                        )
                    ),
                    value = transaction.value
                )
            } else if (transaction.type == "purchase") {
                TransactionRow(
                    type = "Order",
                    date = SimpleDateFormat(BuildConfig.DATE_PATTERN, Locale.GERMAN).format(
                        Date(
                            transaction.timestamp
                        )
                    ),
                    value = transaction.value
                )
            }
        }
        Box(Modifier.height(50.dp))
    }
}

/**
 * Button that makes the QR Code Dialog visible.
 * @param viewModel the transaction ViewModel.
 */
@Composable
fun QRCodeButton(viewModel: ITransactionViewModel) {
    CustomButton(
        text = stringResource(R.string.qrcode),
        fraction = 0.9f,
        onClick = {
            viewModel.qrCodeDialogVisible.value = true
        }
    )
}

/**
 * Pie Chart to display bought items.
 * @param viewModel the transaction ViewModel.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PieChartBoughtItems(viewModel: ITransactionViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .height(150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        coroutineScope.launch {
            viewModel.refreshTransactions()
            viewModel.getPurchasesOfUser()
        }
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                val pieChart = PieChart(context)
                stylePieChart(pieChart)
                pieChart.description.text = UIText.StringResource(R.string.piechart_description).asString(context)
                pieChart.setNoDataText(UIText.StringResource(R.string.piechart_nodata).asString(context))
                pieChart
            },
            update = { pieChart ->
                val listColors = listOf(
                    SummerSky.toArgb(),
                    Cinnabar.toArgb(),
                    MoonYellow.toArgb(),
                    MediumSeaGreen.toArgb(),
                )
                val entries = mutableListOf<PieEntry>()
                val chartMap = mutableMapOf<String, Pair<String, Int>>()
                val purchases = viewModel.purchases
                purchases.forEach { purchase ->
                    if (chartMap.containsKey(purchase.itemId)) {
                        val mapValue = chartMap[purchase.itemId]
                        if (mapValue != null) {
                            chartMap[purchase.itemId] =
                                Pair(purchase.itemName, mapValue.second.plus(purchase.amount))
                        }
                    } else {
                        chartMap[purchase.itemId] = Pair(purchase.itemName, purchase.amount)
                    }
                }
                var sortedChartValues =
                    chartMap.values.sortedWith(compareBy({ it.second }, { it.first }))
                sortedChartValues =
                    if (sortedChartValues.size >= 4) sortedChartValues.asReversed().subList(0, 4)
                    else if (sortedChartValues.isEmpty()) emptyList()
                    else sortedChartValues.asReversed().subList(0, sortedChartValues.size - 1)
                sortedChartValues.forEach {
                    entries.add(PieEntry(it.second.toFloat(), it.first + " (" + it.second + ")"))
                }
                if (entries.size != 0) {
                    val dataset = PieDataSet(entries, "")
                    dataset.colors = listColors
                    dataset.sliceSpace = 3f
                    dataset.setDrawValues(false)
                    dataset.valueTextSize = 7f
                    val pieData = PieData(dataset)
                    pieChart.data = pieData
                }
                pieChart.invalidate()
            }
        )
    }
}

/**
 * Line Chart to display the balance.
 * TODO Last Value is hidden behind view
 * @param viewModel the transaction ViewModel.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LineChartBalance(viewModel: ITransactionViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .height(150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        coroutineScope.launch {
            viewModel.refreshTransactions()
            viewModel.getBalanceOfUser()
        }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val lineChart = LineChart(context)
                lineChart.description.text= UIText.StringResource(R.string.linechart_description).asString(context)
                lineChart.setNoDataText(UIText.StringResource(R.string.linechart_nodata).asString(context))
                styleLineChart(lineChart)
                lineChart.invalidate()
                lineChart
            },
            update = { lineChart ->
                val entries = mutableListOf<Entry>()
                viewModel.balanceList.forEach { pair ->
                    entries.add(Entry(pair.first.toFloat(), pair.second.toFloat()))
                }

                if (entries.size != 0) {
                    val dataset = LineDataSet(entries, "")
                    dataset.axisDependency = YAxis.AxisDependency.LEFT
                    dataset.setDrawValues(false)
                    val lineData = LineData(dataset)
                    lineChart.data = lineData
                }
                lineChart.invalidate()
            }
        )
    }
}

/**
 * Row with a single transaction.
 * @param type the type of the transaction.
 * @param date the date of the transaction.
 * @param value the value of the transaction.
 */
@Composable
fun TransactionRow(type: String, date: String, value: Double) {
    val color = if (value > 0) Color.Green else Color.DarkGray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TransactionCircle(color)
            TransactionType(type)
            TransactionDate(date)
            TransactionValue(color, "${String.format("%.2f", value)}€")
        }
    }
}

/**
 * The type of the transaction.
 * @param type the type of the transaction.
 */
@Composable
fun TransactionType(type: String) {
    Text(
        modifier = Modifier.width(80.dp),
        text = type,
        fontSize = rowTextFontSize,
        textAlign = TextAlign.Start
    )
}

/**
 * Circle with the color of the transaction.
 * @param color
 */
@Composable
fun TransactionCircle(color: Color) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(shape = CircleShape)
            .background(color)
    )
}

/**
 * The date of the transaction.
 * @param date the date of the transaction.
 */
@Composable
fun TransactionDate(date: String) {
    Text(
        modifier = Modifier.width(100.dp),
        text = date,
        fontSize = rowTextFontSize,
        color = Color.Gray
    )
}

/**
 * The value of the transaction.
 * @param color the color of the value.
 * @param value the value of the transaction.
 */
@Composable
fun TransactionValue(color: Color, value: String) {
    Text(
        modifier = Modifier.width(100.dp),
        text = value,
        fontSize = rowTextFontSize,
        color = color,
        textAlign = TextAlign.End
    )
}

/**
 * Function to style the pie chart.
 * @param pieChart the pie chart to style.
 */
fun stylePieChart(pieChart: PieChart) {
    pieChart.holeRadius = 20f
    pieChart.transparentCircleRadius = 25f
    pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
    pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
    pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
    pieChart.setDrawEntryLabels(false)
    pieChart.setDrawCenterText(true)
    pieChart.notifyDataSetChanged()
    pieChart.description.textSize = 10f
    pieChart.description.yOffset = 110f
    pieChart.description.xOffset = -60f
}

/**
 * Function to style the line chart.
 * @param lineChart the line chart to style.
 */
fun styleLineChart(lineChart: LineChart) {
    val formatter = DateTimeFormatter()
    lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    lineChart.xAxis.setDrawGridLines(false)
    lineChart.xAxis.granularity = 2f
    lineChart.xAxis.valueFormatter = formatter
    lineChart.axisLeft.setDrawGridLines(false)
    lineChart.axisRight.isEnabled = false
    lineChart.description.xOffset = 150f
    lineChart.description.yOffset = 100f
}
