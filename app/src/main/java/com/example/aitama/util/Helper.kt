package com.example.aitama.util

import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetTransaction
import java.text.NumberFormat
import java.util.*


fun sumAssetPrice(item: List<AssetTransaction>): Double {

    return item
        .filter { assetTransaction ->
            assetTransaction.transactionType ==
                    TransactionType.BUY && assetTransaction.amount != assetTransaction.amountSold
        }
        .sumOf {
            (it.price * (it.amount - it.amountSold)).toDouble()
        }

}

fun sumAssetPrice(item: AssetDto): Double {

    return sumAssetPrice(item.assetTransactions)

}

fun sumAssetValue(item: AssetDto, order: Int = 0): Double {

    if (item.assetPrices.isNotEmpty()) {
        val amount = sumAssetAmount(item.assetTransactions)
        val price =
            item.assetPrices.sortedByDescending { price -> price.date }[order].price
        return amount * price
    }
    return 0.0

}

fun formatDollar(number: Double?): String {

    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance("USD")
    return format.format(number)
}

fun formatPercentage(number: Double?): String {

    val format = NumberFormat.getPercentInstance()
    format.minimumFractionDigits = 2
    return format.format(number)

}


fun sumAssetAmount(item: List<AssetTransaction>?): Double {

    item?.let {
        return item.sumOf { it.amount.toDouble() }
    }
    return 0.0

}

enum class AssetType {
    STOCK, CRYPTO
}

enum class TransactionType {
    BUY, SELL
}

fun calculatePerformancePercentage(assetDto: AssetDto, position: Int = 0): Double {

    if (sumAssetAmount(assetDto.assetTransactions) > 0) {
        val value = sumAssetValue(assetDto, order = position)
        val price = sumAssetPrice(assetDto.assetTransactions)
        return (value - price) / price
    }
    return 0.0

}

fun calculatePerformancePercentage(assetDtos: List<AssetDto>, position: Int = 0): Double {

    val value = assetDtos
        .sumOf { dto ->
            sumAssetValue(dto, position)
        }

    val price = assetDtos
        .sumOf { dto ->
            sumAssetPrice(dto)
        }

    return (value - price) / price

}

fun calculatePerformanceTotal(assetDto: AssetDto): Double {

    val value = sumAssetValue(assetDto)
    val price = sumAssetPrice(assetDto.assetTransactions)
    return value - price

}
