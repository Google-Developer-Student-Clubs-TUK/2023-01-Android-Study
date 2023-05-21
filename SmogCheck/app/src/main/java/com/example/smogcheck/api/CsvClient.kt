package com.example.smogcheck.api

import android.content.Context
import com.opencsv.CSVReaderBuilder

data class StationData(val category: String?, val name: String?, val address: String?, val Company: String?, val year: String?)
object CsvClient {
    fun readCsvFromResources(context: Context, resourceId: Int): List<StationData> {
        val reader = context.resources.openRawResource(resourceId).bufferedReader()
        val csvReader = CSVReaderBuilder(reader)
            .withSkipLines(1) // Skip the header row
            .build()

        val result = mutableListOf<StationData>()
        var line: Array<String>?
        while (csvReader.readNext().also { line = it } != null) {
            // Process each line of the file here
            val category = line?.get(0)
            val name = line?.get(1)
            val address = line?.get(2)
            val company = line?.get(3)
            val year = line?.get(4)

            result.add(StationData(category, name, address, company, year))
        }

        return result.toList()
    }
}