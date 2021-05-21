package com.example.aitama.util

import com.example.aitama.dataclasses.AssetDetail

fun sumAssetPrice(item: AssetDetail): Double? {

    return item.assetTransactions?.sumOf { it ->
        it.price.toDouble()
    }
}

fun sumAssetAmount(item: AssetDetail): Double? {

    return item.assetTransactions?.sumOf { it ->
        it.amount.toDouble()
    }
}