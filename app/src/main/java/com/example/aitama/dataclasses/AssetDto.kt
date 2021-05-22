package com.example.aitama.dataclasses

import androidx.lifecycle.LiveData


data class AssetDto(
    val asset: Asset,
    val assetTransactions: List<AssetTransaction>?
)
