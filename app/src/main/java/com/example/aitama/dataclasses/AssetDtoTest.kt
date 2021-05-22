package com.example.aitama.dataclasses

import androidx.room.Embedded
import androidx.room.Relation

data class AssetDtoTest(

    @Embedded
    val asset: Asset,
    @Relation(parentColumn = "symbol", entityColumn = "symbol")
    val assetTransactions: List<AssetTransaction>

)
