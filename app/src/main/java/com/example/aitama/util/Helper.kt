package com.example.aitama.util

import com.example.aitama.dataclasses.AssetTransaction

fun sumAssetPrice(item: List<AssetTransaction>): Double {

    return item.sumOf { it.price.toDouble() }

}

fun sumAssetAmount(item: List<AssetTransaction>): Double {

    return item.sumOf { it.amount.toDouble() }
}

enum class AssetType {
    STOCK, CRYPTO
}