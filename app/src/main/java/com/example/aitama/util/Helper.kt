package com.example.aitama.util

import com.example.aitama.dataclasses.AssetDto

fun sumAssetPrice(item: AssetDto): Double? {

    return item.assetTransactions?.sumOf { it ->
        it.price.toDouble()
    }
}

fun sumAssetAmount(item: AssetDto): Double? {

    return item.assetTransactions?.sumOf { it ->
        it.amount.toDouble()
    }
}