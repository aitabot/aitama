package com.example.aitama.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aitama.dataclasses.AssetTransaction

@Dao
interface AssetTransactionDao {

    @Insert
    fun insert(transaction: AssetTransaction)

    @Update
    fun update(transaction: AssetTransaction)

    @Delete
    fun delete(transaction: AssetTransaction)

    @Query("delete from transaction_table where symbol = :symbol")
    fun deleteAllTransactionsForAsset(symbol: String)


    @Query("select * from transaction_table")
    fun getAllTransactions(): LiveData<List<AssetTransaction>>

    @Query("select * from transaction_table where symbol = :symbol")
    fun getAllTransactionsForAsset(symbol: String): LiveData<List<AssetTransaction>>


}