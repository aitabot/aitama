package com.example.aitama.dataclasses

data class AssetDetail(
    val asset: Asset,
    val assetTransactions: List<AssetTransaction>?
)
