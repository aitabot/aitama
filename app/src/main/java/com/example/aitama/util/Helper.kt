package com.example.aitama.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetPrice
import com.example.aitama.dataclasses.AssetTransaction
import kotlinx.parcelize.RawValue
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


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

fun sumTransactions(item: List<AssetTransaction>): Double {

    val purchases =
        item.filter { assetTransaction -> assetTransaction.transactionType == TransactionType.BUY }
            .sumOf { (it.price * it.amount * -1).toDouble() }

    val sales =
        item.filter { assetTransaction -> assetTransaction.transactionType == TransactionType.SELL }
            .sumOf { abs((it.price * it.amount).toDouble()) }

    return purchases + sales


}

fun sumAmountOfPurchasedAssets(item: List<AssetTransaction>): Double {

    return item.filter { assetTransaction -> assetTransaction.transactionType == TransactionType.BUY }
        .sumOf { (it.amount - it.amountSold).toDouble() }
}


fun sumAssetRevenues(item: List<AssetTransaction>): Double {

    return item
        .filter { assetTransaction ->
            assetTransaction.transactionType ==
                    TransactionType.SELL
        }
        .sumOf {
            (it.price * it.amount * -1).toDouble()
        }

}


fun sumAssetPrice(item: AssetDto): Double {

    return sumAssetPrice(item.assetTransactions)

}

fun assetPrice(item: AssetDto): Double {
    return item.assetPrices[item.assetPrices.size - 1].price.toDouble()
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

    number?.let {
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("USD")
        return format.format(number)
    }
    return "0"
}

fun formatTwoDigits(number: Double?): String {
    val format = NumberFormat.getInstance()
    format.minimumFractionDigits = 2
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
        val value = (sumAssetValue(assetDto, order = position) * 100).roundToInt() / 100.0
        val price = (sumAssetPrice(assetDto.assetTransactions) * 100).roundToInt() / 100.0
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


fun hideKeyboard(context: Context, view: View) {

    val imm: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)

}
