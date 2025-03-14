package com.example.stocktracker

data class Stock(
    val symbol: String,
    val name: String,
    val price: Double?, // Nullable as it might be missing
    val currency: String?, // Nullable as it might be missing
    val stockExchange: String?,
    val exchangeShortName: String?
)