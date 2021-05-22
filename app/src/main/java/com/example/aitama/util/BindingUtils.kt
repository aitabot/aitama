package com.example.aitama.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetTransaction
import java.text.SimpleDateFormat


@BindingAdapter("totalAssetAmount")
fun TextView.setTotalAssetAmount(item: List<AssetDto>?) {
    // todo change to strings.xml
    item?.let {
        text = "${it.size} Assets"
    }
}

@BindingAdapter("totalStockAmount")
fun TextView.setTotalStockAmount(item: List<AssetDto>?) {
    // todo change to strings.xml
    item?.let {
        val amount = it.filter { assetDto -> assetDto.asset.type == AssetType.STOCK }.count()
        text = "${amount} Stocks"
    }
}

@BindingAdapter("totalCryptoAmount")
fun TextView.setTotalCryptoAmount(item: List<AssetDto>?) {
    // todo change to strings.xml
    item?.let {
        val amount = it.filter { assetDto -> assetDto.asset.type == AssetType.CRYPTO }.count()
        text = "${amount} Crypto"
    }
}

@BindingAdapter("totalCryptoPrice")
fun TextView.setTotalCryptoPrice(item: List<AssetDto>?) {
    // todo change to strings.xml
    item?.let {

        val cryptoSum =
            it.filter { assetDto -> assetDto.asset.type == AssetType.CRYPTO }
                .sumOf { dto ->
                    dto.assetTransactions
                        .sumOf { transaction -> transaction.price.toDouble() }
                }

        text = cryptoSum.toString()
    }
}

@BindingAdapter("totalStockPrice")
fun TextView.setTotalStockPrice(item: List<AssetDto>?) {
    item?.let {

        val stockSum =
            it.filter { assetDto -> assetDto.asset.type == AssetType.STOCK }
                .sumOf { dto ->
                    dto.assetTransactions
                        .sumOf { transaction -> transaction.price.toDouble() }
                }

        text = stockSum.toString()
    }
}


@BindingAdapter("totalPrice")
fun TextView.setTotalPrice(item: List<AssetDto>?) {
    item?.let {

        val sum =
            it.sumOf { dto ->
                dto.assetTransactions
                    .sumOf { transaction -> transaction.price.toDouble() }
            }

        text = sum.toString()
    }
}


@BindingAdapter("assetPriceSummed")
fun TextView.setAssetPriceSummed(item: AssetDto?) {
    // todo muss noch verglichen werden mit dem aktuellen preis
    item?.let { text = sumAssetPrice(item.assetTransactions).toString() }

}

@BindingAdapter("assetPriceSummed")
fun TextView.setAssetPriceSummed(item: List<AssetTransaction>?) {
    // todo muss noch verglichen werden mit dem aktuellen preis
    item?.let { text = sumAssetPrice(item).toString() }

}

@BindingAdapter("assetAmountSummed")
fun TextView.setAssetAmountSummed(item: AssetDto?) {
    item?.let {
        // todo change to string resources
        text = "${sumAssetAmount(item.assetTransactions)} pcs."
    }
}

@BindingAdapter("assetAmountSummed")
fun TextView.setAssetAmountSummed(item: List<AssetTransaction>?) {
    item?.let {
        // todo change to string resources
        text = "${sumAssetAmount(item)} pcs."
    }
}


@BindingAdapter("assetName")
fun TextView.setAssetName(item: AssetDto?) {
    item?.let {
        text = item.asset.name
    }
}

@BindingAdapter("assetName")
fun TextView.setAssetName(item: Asset?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("assetSymbol")
fun TextView.setAssetSymbol(item: AssetDto?) {

    item?.let {
        text = item.asset.symbol
    }
}

@BindingAdapter("assetSymbol")
fun TextView.setAssetSymbol(item: Asset?) {

    item?.let {
        text = item.symbol
    }
}

@BindingAdapter("assetPercentage")
fun TextView.setAssetPercentage(item: AssetDto?) {
    // todo compare to actual prices, then calculate difference in %
    item?.let {
        text = "100 %"
    }
}

@BindingAdapter("assetPercentage")
fun TextView.setAssetPercentage(item: Asset?) {
    // todo compare to actual prices, then calculate difference in %
    item?.let {
        text = "100 %"
    }
}

@BindingAdapter("transactionDate")
fun TextView.setTransactionDate(item: AssetTransaction?) {
    item?.let {
        val date = SimpleDateFormat.getDateInstance().format(item.date)
        text = date.toString()
    }
}

@BindingAdapter("transactionAmount")
fun TextView.setTransactionAmount(item: AssetTransaction?) {
    item?.let {
        //todo change to string resources
        //todo add + or - depending if amount is negative, take absolute amount
        text = "${item.amount} pcs."
    }
}

@BindingAdapter("transactionPrice")
fun TextView.setTransactionPrice(item: AssetTransaction?) {
    item?.let {
        // todo change to string resources
        // todo include currency in data set?
        text = "€ ${(item.price * item.amount)}"
    }
}



