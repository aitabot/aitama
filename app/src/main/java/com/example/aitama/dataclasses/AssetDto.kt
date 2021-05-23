package com.example.aitama.dataclasses

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class AssetDto(

    @Embedded val asset: @RawValue Asset,
    @Relation(parentColumn = "symbol", entityColumn = "symbol")
    val assetTransactions: List<@RawValue AssetTransaction>,
    @Relation(parentColumn = "symbol", entityColumn = "symbol")
    val assetPrices: List<@RawValue AssetPrice>


) : Parcelable
