package com.example.stocktracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StockRepository(private val stockDao: StockDao) {

        private val apiService = StockApiService()

        fun getAllFollowedStocks(): Flow<List<Stock>> {
            return stockDao.getAll().map { list -> list.map { it.toStock() } }
        }

        suspend fun addStockToFollow(stock: Stock) {
            stockDao.insert(StockEntity(stock.symbol, stock.name))
        }

        suspend fun removeStockFromFollow(symbol: String) {
            stockDao.delete(symbol)
        }

        suspend fun searchStocks(query: String): List<Stock> = apiService.searchStocks(query)

        suspend fun getStockPrice(symbol: String): Double? = apiService.getStockQuote(symbol)
}

fun StockEntity.toStock(): Stock {
    return Stock(symbol, name, null, null, null, null)
}