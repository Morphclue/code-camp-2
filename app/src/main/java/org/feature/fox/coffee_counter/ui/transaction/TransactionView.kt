package org.feature.fox.coffee_counter.ui.transaction

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import java.text.SimpleDateFormat
import java.util.*

private val rowTextFontSize: TextUnit = 18.sp
val purchaseList = listOf<Purchase>(
    Purchase(123456789, "HansID", 42.0, "itemID", "Espresso", 2),
    Purchase(123456790, "HansID", 4.0, "itemID2", "Coffee", 15),
    Purchase(123456791, "HansID", 123.0, "itemID3", "Tea", 46),
    Purchase(123456792, "HansID", 21.0, "itemID1", "Espresso", 1)
)
@Preview(showSystemUi = true)
@Composable
fun HistoryViewPreview(
) {
    HistoryView(viewModel = TransactionViewModelPreview())
}

@Composable
fun HistoryView(
    viewModel: ITransactionViewModel,
) {
    Column {
        MoneyAppBar(Pair(stringResource(R.string.history_title), viewModel.balance))
        ShowPeriodField()
        PieChartBoughtItems(purchaseList as MutableList<Purchase>)
        BarChartExpenses(purchaseList as MutableList<Purchase>)
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
        if (viewModel.transactions.isEmpty()) Text(
            stringResource(id = R.string.no_data),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.LightGray
        )


        viewModel.transactions.forEach { transaction ->
            if (transaction.type == "funding") {
                TransactionRow(
                    Color.Green,
                    "Funding",
                    SimpleDateFormat(BuildConfig.DATE_PATTERN, Locale.GERMAN)
                        .format(Date(transaction.timestamp)),
                    "${String.format("%.2f", transaction.value)}€"
                )
            } else if (transaction.type == "purchase") {
                TransactionRow(
                    Color.DarkGray,
                    "Order",
                    SimpleDateFormat(BuildConfig.DATE_PATTERN, Locale.GERMAN)
                        .format(Date(transaction.timestamp)),
                    "${String.format("%.2f", transaction.value)}€"
                )
            }
        }
    }
}

@Composable
fun ShowPeriodField() {
    // TODO: implement searchbar
}

@Composable
fun PieChartBoughtItems(data: MutableList<Purchase>){

    Column(
        modifier = Modifier.height(150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                val listColors = ArrayList<Int>()
                listColors.add(Color.Red.toArgb())
                listColors.add(Color.Green.toArgb())
                listColors.add(Color.Yellow.toArgb())
                listColors.add(Color.Blue.toArgb())
                val pieChart = PieChart(context)
                val entries = listOf(
                    PieEntry(18.5f, "Green"),
                    PieEntry(26.7F, "Yellow"),
                    PieEntry(24.0f, "Red"),
                    PieEntry(30.8f, "Blue")
                )
                val dataset = PieDataSet(entries, "")
                dataset.colors = listColors
                //val dataset = PieDataSet(entries, "LABEL").apply { color = Color.Red.toArsgb() }
                val pieData = PieData(dataset)
                pieChart.data = pieData
                pieChart.setUsePercentValues(true)
                pieChart.description.isEnabled = false
                pieChart.invalidate()

                pieChart
            }
        )
    }
}

@Composable
fun BarChartExpenses(data: MutableList<Purchase>) {
    Column(
        modifier = Modifier.height(150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val listColors = ArrayList<Int>()
                listColors.add(Color.Red.toArgb())
                listColors.add(Color.Green.toArgb())
                listColors.add(Color.Yellow.toArgb())
                listColors.add(Color.Blue.toArgb())
                val barChart = BarChart(context)

                val entries = listOf(
                    BarEntry(0f, 20.0f),
                    BarEntry(1f, 10.0f),
                    BarEntry(3f, 5.0f),
                    BarEntry(4f, 50.0f),
                )

                val barDataSet = BarDataSet(entries, "BLA").apply { colors = listColors }

                val barData = BarData(barDataSet)
                barChart.data = barData
                barChart.invalidate()

                barChart
            }
        )
    }
}

@Composable
fun TransactionRow(color: Color, type: String, date: String, value: String) {
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
            TransactionValue(color, value)
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
        modifier = Modifier.width(80.dp),
        text = value,
        fontSize = rowTextFontSize,
        color = color,
        textAlign = TextAlign.End
    )
}
