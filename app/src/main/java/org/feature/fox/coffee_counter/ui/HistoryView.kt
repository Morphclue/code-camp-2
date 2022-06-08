package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import java.text.SimpleDateFormat
import java.util.*

private val rowTextFontSize: TextUnit = 18.sp
private const val datePattern = "dd.MM.yy"
private val transactions = listOf(
    Funding(1654153006000, "foo", 10.00),
    Purchase(1645167406000, "foo", -9.89, "003", "Espresso", 3),
    Purchase(1613285806000, "foo", -3.30, "001", "Cola", 2),
)

@Preview(showSystemUi = true)
@Composable
fun HistoryView() {
    Column {
        MoneyAppBar(title = stringResource(R.string.history_title))
        ShowPeriodField()
        TransactionContainer()
    }
}

@Composable
fun TransactionContainer() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(5.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        transactions.forEach { transaction ->
            if (transaction is Funding) {
                TransactionRow(
                    Color.Green,
                    "Funding",
                    SimpleDateFormat(datePattern, Locale.GERMAN)
                        .format(Date(transaction.timestamp)),
                    "${transaction.value}€"
                )
            } else if (transaction is Purchase) {
                TransactionRow(
                    Color.DarkGray,
                    "Order",
                    SimpleDateFormat(datePattern, Locale.GERMAN)
                        .format(Date(transaction.timestamp)),
                    "${transaction.totalValue}€"
                )
            }
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun ShowPeriodField() {
    // TODO: implement searchbar
}

@Composable
fun TransactionRow(color: Color, type: String, date: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        TransactionCircle(color)
        TransactionType(type)
        TransactionDate(date)
        TransactionValue(color, value)
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
