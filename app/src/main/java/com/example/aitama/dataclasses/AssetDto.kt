package com.example.aitama.dataclasses

import androidx.room.Embedded
import androidx.room.Relation


data class AssetDto(

    @Embedded val asset: Asset,
    @Relation(parentColumn = "symbol", entityColumn = "symbol")
    val assetTransactions: List<AssetTransaction>,
    @Relation(parentColumn = "symbol", entityColumn = "symbol")
    val assetPrices: List<AssetPrice>


)
