package com.example.smogcheck.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.smogcheck.R
import com.example.smogcheck.api.ApiData
import com.example.smogcheck.api.ApiSubscriber
import com.example.smogcheck.api.CsvClient
import com.example.smogcheck.api.Item
import com.example.smogcheck.api.StationData
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MainPage () {
    val context = LocalContext.current

    var address by remember { mutableStateOf("종로구") }
    var addressList by remember { mutableStateOf(
        mutableListOf(
            StationData(
            "서울시",
            "종로구",
            "서울 종로구 종로35가길 19 종로5,6가 동 주민센터",
            "서울특별시보건환경연구원",
            "1997")
        ))
    }

    var data by remember { mutableStateOf<ApiData?>(null) }

    Column (Modifier.fillMaxSize()) {
        var expanded by remember { mutableStateOf(false) }
        Box (
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .wrapContentSize(Alignment.TopStart)) {
            Button(onClick = { expanded = true }) {
                Text(address)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}) {
                for (d in addressList) {
                    DropdownMenuItem(
                        text = { Text("${d.category} - ${d.name}") },
                        onClick = { address = d.name!!; expanded = false})
                }
            }
        }
        data?.let {
            MainPageData(data!!)
        } ?: run {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
    LaunchedEffect(Unit) {
        addressList = CsvClient.readCsvFromResources(context, R.raw.station_list).toMutableList()
    }
    LaunchedEffect(address) {
        data = null
        data = ApiSubscriber.run(address)
    }
}

@Composable
fun MainPageData (data: ApiData) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
        ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(data.response.body.items.last().khaiValue,
                fontSize = 42.sp)
            GradeGuide(grade = data.response.body.items.last().khaiGrade.toInt())
        }
        val chartEntryModel = entryModelOf(data.response.body.items
            .mapIndexed {idx, d -> FloatEntry(idx.toFloat(),  d.khaiValue.toFloat()) })
        Chart(
            chart = lineChart(),
            model = chartEntryModel,
        )

        DataTable(
            listData = data.response.body.items.reversed(),
            modifier = Modifier.padding(0.dp, 100.dp, 0.dp, 0.dp))
    }
}
@Composable
fun DataTable(listData: List<Item>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        FullArrayRow(index = "", data = listData.map { d -> useOnlyTime(d.dataTime) })
        FullArrayRow(index = "아황산가스", data = listData.map { d -> d.so2Value })
        FullArrayRow(index = "일산화탄소", data = listData.map { d -> d.coValue })
        FullArrayRow(index = "이산화질소", data = listData.map { d -> d.no2Value })
        FullArrayRow(index = "오존", data = listData.map { d -> d.o3Value })
        FullArrayRow(index = "미세먼지", data = listData.map { d -> d.pm10Value })
        FullArrayRow(index = "초미세먼지", data = listData.map { d -> d.pm25Value })
    }
}
fun useOnlyTime(dayString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val require = DateTimeFormatter.ofPattern("HH:mm")
    val dateTime = LocalDateTime.parse(dayString, formatter)
    return dateTime.format(require)
}
@Composable
fun FullArrayRow(index: String, data: List<String>) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color.Black,
                    strokeWidth = 1.dp.toPx(),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f)
                )
            }){
        Text(text = index, modifier = Modifier.fillMaxWidth(.2f))
        for (d: String in data) {
            Text(text = d, modifier = Modifier.width(60.dp))
        }
    }
}
@Composable
fun GradeGuide(grade: Int) {
    val imgSrc = when(grade) {
        1 -> "https://www.airkorea.or.kr/web/images/sub/211_character01.png"
        2 -> "https://www.airkorea.or.kr/web/images/sub/211_character02.png"
        3 -> "https://www.airkorea.or.kr/web/images/sub/211_character03.png"
        4 -> "https://www.airkorea.or.kr/web/images/sub/211_character04.png"
        else -> ""
    }
    val text = when(grade) {
        1 -> "좋음"
        2 -> "보틍"
        3 -> "나쁨"
        4 -> "매우 나쁨"
        else -> "오류"
    }
    Row {
        Text(text)
        AsyncImage (model = imgSrc, contentDescription = null)
    }
}

@Composable
@Preview
fun DataColumnPreview() {
    MainPageData(data = ApiSubscriber.mock()!!)
}