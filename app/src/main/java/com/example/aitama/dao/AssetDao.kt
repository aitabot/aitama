package com.example.aitama.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aitama.dataclasses.Asset

@Dao
interface AssetDao {

    @Insert
    fun insert(asset: Asset)

    @Update
    fun update(asset: Asset)

    @Delete
    fun delete(asset: Asset)

    @Query("select * from asset_table")
    fun getAllAssets(): LiveData<List<Asset>>

    @Query("select * from asset_table where symbol == :symbol")
    fun getAssetBySymbol(symbol: String): LiveData<List<Asset>>

}