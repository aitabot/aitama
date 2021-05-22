package com.example.aitama.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.Relation


data class AssetDto(

    @Embedded val asset: Asset,
    @Relation(parentColumn = "symbol", entityColumn = "symbol")
    val assetTransactions: List<AssetTransaction>


)
