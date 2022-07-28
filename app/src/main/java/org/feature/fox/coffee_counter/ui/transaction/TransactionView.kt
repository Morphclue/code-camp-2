package org.feature.fox.coffee_counter.ui.transaction

import android.annotation.SuppressLint
import android.util.Log
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
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.ui.common.CustomButton
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.util.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

private val rowTextFontSize: TextUnit = 18.sp


@Preview(showSystemUi = true)
@Composable
fun HistoryViewPreview(
) {
    HistoryView(viewModel = TransactionViewModelPreview())
}

// TODO update orders (Issue)
@Composable
fun HistoryView(
    viewModel: ITransactionViewModel,
) {
    QRCodeDialog(viewModel)
    Column {
        MoneyAppBar(Pair(stringResource(R.string.history_title), viewModel.balance))
        PieChartBoughtItems(data = viewModel.purchases)
        LineChartBalance(data = viewModel.balanceList)
        TransactionContainer(viewModel)
    }
}

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
        QRCodeButton(viewModel)
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
                    date = SimpleDateFormat(BuildConfig.DATE_PATTERN, Locale.GERMAN).format(Date(transaction.timestamp)),
                    value = transaction.value
                )
            } else if (transaction.type == "purchase") {
                TransactionRow(
                    type =  "Order",
                    date = SimpleDateFormat(BuildConfig.DATE_PATTERN, Locale.GERMAN).format(Date(transaction.timestamp)),
                    value = transaction.value
                )
            }
        }
        Box(Modifier.height(50.dp))
    }
}

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

// TODO: Maybe add detailed PieChart for total value of each category
@Composable
fun PieChartBoughtItems(data: MutableList<Purchase>){
    println("Size of purchases: ${data.size}")
    Column(
        modifier = Modifier
            .height(150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                Log.e("PURCHASE", "Size inside piechart: ${data.size}")
                val listColors = ArrayList<Int>()
                listColors.add(Color(52, 152, 219, 255).toArgb()) //Blue
                listColors.add(Color(230, 76, 59, 255).toArgb()) //Red
                listColors.add(Color(241, 196, 15, 255).toArgb()) //Yellow
                listColors.add(Color(46, 204, 112, 255).toArgb()) //Green
                val entries = mutableListOf<PieEntry>()
                var chartMap = mutableMapOf<String, Pair<String, Int>>()
                data.forEach { purchase ->
                    if (chartMap.containsKey(purchase.itemId)) {
                        val mapValue = chartMap[purchase.itemId]
                        chartMap[purchase.itemId] =
                            Pair(purchase.itemName, mapValue!!.second?.plus(purchase.amount))
                    } else {
                        chartMap[purchase.itemId] = Pair(purchase.itemName, purchase.amount)
                    }
                }
                chartMap.forEach {
                    entries.add(PieEntry(it.value.second.toFloat(), it.value.first))
                }
                val pieChart = PieChart(context)
                val dataset = PieDataSet(entries, "")
                dataset.colors = listColors
                dataset.sliceSpace = 3f
                dataset.valueTextSize = 7f

                val pieData = PieData(dataset)
                pieChart.data = pieData
                pieChart.setUsePercentValues(false)
                // Cirlce Styling
                pieChart.holeRadius = 20f
                pieChart.transparentCircleRadius = 25f
                // Legend Styling
                pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
                pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

                pieChart.setDrawEntryLabels(false)

                pieChart.description.text = "Consumed Items"
                pieChart.description.textSize = 10f
                pieChart.description.yOffset = 110f
                pieChart.description.xOffset = -60f
                pieChart.setNoDataText("No Purchases found")
                pieChart.invalidate()

                pieChart
            },
            update = { view ->
                view.invalidate()
            }
        )
    }
}

//TODO format timestamps
@Composable
fun LineChartBalance(data: MutableList<Pair<Long, Double>>) {
    Column(
        modifier = Modifier
            .height(150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val lineChart = LineChart(context)
                val entries = mutableListOf<Entry>()
                data.forEach { pair ->
                    entries.add(Entry(pair.first.toFloat(), pair.second.toFloat()))
                }
                val formatter = DateTimeFormatter()
                val dataset = LineDataSet(entries, "")
                dataset.axisDependency = YAxis.AxisDependency.LEFT
                val lineData = LineData(dataset)
                lineChart.data = lineData
                lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                lineChart.xAxis.setDrawGridLines(false)
                lineChart.xAxis.granularity = 2f
                //lineChart.xAxis.valueFormatter = LargeValueFormatter()
                lineChart.xAxis.valueFormatter = formatter
                lineChart.axisLeft.setDrawGridLines(false)
                lineChart.axisRight.isEnabled = false
                lineChart.invalidate()

                lineChart
            }
        )
    }
}

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

@Composable
fun TransactionType(type: String) {
    Text(
        modifier = Modifier.width(80.dp),
        text = type,
        fontSize = rowTextFontSize,
        textAlign = TextAlign.Start
    )
}

@Composable
fun TransactionCircle(color: Color) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(shape = CircleShape)
            .background(color)
    )
}

@Composable
fun TransactionDate(date: String) {
    Text(
        modifier = Modifier.width(100.dp),
        text = date,
        fontSize = rowTextFontSize,
        color = Color.Gray
    )
}

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
