package com.example.aitama.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aitama.dataclasses.AssetPrice

@Dao
interface AssetPriceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(assetPrice: AssetPrice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(assetPrice: List<AssetPrice>)

    @Update
    fun update(assetPrice: AssetPrice)

    @Delete
    fun delete(assetPrice: AssetPrice)

    @Query("select * from price_table")
    fun getAllAssetPrices(): LiveData<List<AssetPrice>>

    @Query("select * from price_table where symbol = :symbol")
    fun getAllAssetPricesForSymbol(symbol: String): LiveData<List<AssetPrice>>

    @Query("select * from price_table where symbol = :symbol ORDER BY date DESC LIMIT 1 ")
    fun getLatestAssetPriceForSymbol(symbol: String): LiveData<AssetPrice>

}