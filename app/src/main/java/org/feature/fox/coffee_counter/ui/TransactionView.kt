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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val rowTextFontSize: TextUnit = 18.sp

@Preview(showSystemUi = true)
@Composable
fun TransactionView() {
    Column {
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
        // FIXME: iterate over Transactions
        TransactionRow(
            Color.Green,
            "Funding",
            "01.06.2022",
            "+10,00€"
        )
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp
        )
        TransactionRow(
            Color.DarkGray,
            "Order",
            "03.06.2022",
            "-9,89€"
        )
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
