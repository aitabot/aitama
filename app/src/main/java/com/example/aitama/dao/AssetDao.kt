package com.example.aitama.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetDto

@Dao
interface AssetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(asset: Asset)

    @Update
    fun update(asset: Asset)

    @Delete
    fun delete(asset: Asset)

    @Query("select * from asset_table")
    fun getAllAssets(): LiveData<List<Asset>>

    @Query("select * from asset_table where symbol = :symbol")
    fun getAssetBySymbol(symbol: String): LiveData<Asset>

    @Query("select symbol from asset_table")
    fun getAssetSymbols(): List<String>

    @Transaction
    @Query("select * from asset_table")
    fun getAllAssetDto(): LiveData<List<AssetDto>>

    @Query("select * from asset_table where symbol = :symbol")
    fun getLiveDataAssetDto(symbol: String): LiveData<AssetDto>

    @Query("select * from asset_table where symbol = :symbol")
    fun getAssetDto(symbol: String): AssetDto

    @Query("select exists(select * from asset_table where symbol = :symbol)")
    fun assetExists(symbol: String): Boolean
}