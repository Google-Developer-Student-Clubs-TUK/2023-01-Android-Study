package com.example.smogcheck.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

object ApiSubscriber {
    suspend fun run(path: String): ApiData? {
        var uri = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty"
        uri += "?serviceKey=" + "k66AXycu%2FbLdN1ZVW%2Btub9i0mKciLvU%2BeCysPW0o89T4RtPms6TAGjgvdf9biEtqZvN3Xo%2F2WxMgCoiXoxiFMA%3D%3D" // key
        uri += "&returnType=" + "json"
        uri += "&numOfRows=" + "5"
        uri += "&pageNo=" + "1"
        uri += "&stationName=" + URLEncoder.encode(path, "UTF-8")
        uri += "&dataTerm=" + "DAILY"
        uri += "&ver=" + "1.0"

        return kotlin.runCatching {
            KtorClient.client
                .get(uri)
                .body<ApiData>()
        }.getOrNull()
    }
    fun mock(): ApiData? {
        val data = """{"response":{"body":{"totalCount":23,"items":[{"so2Grade":"1","coFlag":null,"khaiValue":"66","so2Value":"0.002","coValue":"0.3","pm25Flag":null,"pm10Flag":null,"pm10Value":"48","o3Grade":"2","khaiGrade":"2","pm25Value":"10","no2Flag":null,"no2Grade":"1","o3Flag":null,"pm25Grade":"1","so2Flag":null,"dataTime":"2023-05-21 10:00","coGrade":"1","no2Value":"0.007","pm10Grade":"1","o3Value":"0.048"},{"so2Grade":"1","coFlag":null,"khaiValue":"63","so2Value":"0.002","coValue":"0.3","pm25Flag":null,"pm10Flag":null,"pm10Value":"31","o3Grade":"2","khaiGrade":"2","pm25Value":"8","no2Flag":null,"no2Grade":"1","o3Flag":null,"pm25Grade":"1","so2Flag":null,"dataTime":"2023-05-21 09:00","coGrade":"1","no2Value":"0.008","pm10Grade":"1","o3Value":"0.045"},{"so2Grade":"1","coFlag":null,"khaiValue":"61","so2Value":"0.002","coValue":"0.3","pm25Flag":null,"pm10Flag":null,"pm10Value":"19","o3Grade":"2","khaiGrade":"2","pm25Value":"6","no2Flag":null,"no2Grade":"1","o3Flag":null,"pm25Grade":"1","so2Flag":null,"dataTime":"2023-05-21 08:00","coGrade":"1","no2Value":"0.009","pm10Grade":"1","o3Value":"0.042"},{"so2Grade":"1","coFlag":null,"khaiValue":"64","so2Value":"0.002","coValue":"0.3","pm25Flag":null,"pm10Flag":null,"pm10Value":"13","o3Grade":"2","khaiGrade":"2","pm25Value":"4","no2Flag":null,"no2Grade":"1","o3Flag":null,"pm25Grade":"1","so2Flag":null,"dataTime":"2023-05-21 07:00","coGrade":"1","no2Value":"0.006","pm10Grade":"1","o3Value":"0.047"},{"so2Grade":"1","coFlag":null,"khaiValue":"65","so2Value":"0.002","coValue":"0.3","pm25Flag":null,"pm10Flag":null,"pm10Value":"10","o3Grade":"2","khaiGrade":"2","pm25Value":"4","no2Flag":null,"no2Grade":"1","o3Flag":null,"pm25Grade":"1","so2Flag":null,"dataTime":"2023-05-21 06:00","coGrade":"1","no2Value":"0.005","pm10Grade":"1","o3Value":"0.047"}],"pageNo":1,"numOfRows":5},"header":{"resultMsg":"NORMAL_CODE","resultCode":"00"}}}"""

        return Json.decodeFromString(data)
    }
}