package com.example.aitama.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asset_table")
data class Asset(

    @PrimaryKey
    val symbol: String,
    @ColumnInfo(name = "asset_name")
    val name: String,
    @ColumnInfo(name = "asset_isin")
    val isin: String = "",
    @ColumnInfo(name = "asset_type")
    val type: String
)
