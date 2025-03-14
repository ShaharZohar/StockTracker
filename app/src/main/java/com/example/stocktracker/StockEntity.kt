package com.example.stocktracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StockEntity(
    @PrimaryKey val symbol: String,
    val name: String
)
