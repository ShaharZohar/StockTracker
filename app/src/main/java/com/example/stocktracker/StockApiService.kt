package com.example.stocktracker


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StockApiService {

    suspend fun searchStocks(query: String): List<Stock> = withContext(Dispatchers.IO) {
        try {
            val url = "https://financialmodelingprep.com/api/v3/search?query=$query&apikey=API_KEY"

            val connection = Jsoup.connect(url).ignoreContentType(true)
            val document = connection.get()
            println(document.body().text())
            val responseJson = document.body().text().trimIndent()

            val results = parseStockSearchResults(responseJson)
            results.forEach {
                println("Symbol: ${it.symbol}, Name: ${it.name}")
            }

            results
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getStockQuote(symbol: String): Double? = withContext(Dispatchers.IO) {
        try {
            val url = "https://financialmodelingprep.com/api/v3/quote-short/$symbol?apikey=API_KEY"
            val connection = Jsoup.connect(url).ignoreContentType(true)
            val document = connection.get()
            val responseJson = document.body().text().trimIndent()
            val results = parseStockSearchResults(responseJson)
            results.forEach {
                println("Symbol: ${it.symbol}, Name: ${it.name}")
            }

            results.firstOrNull()?.price
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseStockSearchResults(json: String): List<Stock> {
        val gson = Gson()
        val listType = object : TypeToken<List<Stock>>() {}.type
        return gson.fromJson(json, listType)
    }

}