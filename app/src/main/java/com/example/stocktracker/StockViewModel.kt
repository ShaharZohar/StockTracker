package com.example.stocktracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StockViewModel(private val repository: StockRepository) : ViewModel() {

    private val _followedStocks = MutableLiveData<List<Stock>>()
    val followedStocks: LiveData<List<Stock>> = _followedStocks

    private val _searchResults = MutableLiveData<List<Stock>>()
    val searchResults: LiveData<List<Stock>> = _searchResults

    fun loadFollowedStocks() {
        viewModelScope.launch {
            repository.getAllFollowedStocks().collectLatest { stocks ->
                val prices = stocks.map { stock ->
                    async(Dispatchers.IO) {
                        stock.copy(price = repository.getStockPrice(stock.symbol))
                    }
                }.map { it.await() }
                _followedStocks.value = prices
            }
        }
    }

    fun searchStocks(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val results = repository.searchStocks(query)
            _searchResults.postValue(results)
        }
    }

    fun addStockToFollow(stock: Stock) {
        viewModelScope.launch {
            repository.addStockToFollow(stock)
        }
    }

    fun removeStockFromFollow(symbol: String) {
        viewModelScope.launch {
            repository.removeStockFromFollow(symbol)
        }
    }

    fun isStockFollowed(symbol: String): Boolean {
        return _followedStocks.value?.any { it.symbol == symbol } ?: false
    }
}
