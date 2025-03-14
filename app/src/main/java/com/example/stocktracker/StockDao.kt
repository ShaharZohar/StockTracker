package com.example.stocktracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM stockentity")
    fun getAll(): Flow<List<StockEntity>>

    @Insert
    suspend fun insert(stock: StockEntity)

    @Query("DELETE FROM stockentity WHERE symbol = :symbol")
    suspend fun delete(symbol: String)
}
