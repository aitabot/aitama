package com.example.aitama.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(
    tableName = "price_table",
//    indices = [Index(value = ["date", "symbol"], unique = true)],
    primaryKeys = ["date", "symbol"]
)
data class AssetPrice(

    @ColumnInfo(name = "date")
    val date: Date,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "updated_date")
    val updatedDate: Date,

    )