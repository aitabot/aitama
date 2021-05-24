package com.example.aitama.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transaction_table")
data class AssetTransaction(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val transactionId: Long = 0L,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    // todo check if float is enough for e.g. BTC -> Kommastellen, amount is number of pieces
    @ColumnInfo(name = "amount")
    val amount: Float,
    @ColumnInfo(name = "price")
    val price: Float,
    @ColumnInfo(name = "date")
    val date: Date

)
