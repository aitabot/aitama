package com.example.aitama.dataclasses


data class AssetDto(
    val asset: Asset,
    val assetTransactions: List<AssetTransaction>?
)
