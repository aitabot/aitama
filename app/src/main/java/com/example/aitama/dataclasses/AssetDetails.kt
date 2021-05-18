package com.example.aitama.dataclasses

data class AssetDetails(
    val asset: Asset,
    val assetTransactions: List<AssetTransaction>?
)
