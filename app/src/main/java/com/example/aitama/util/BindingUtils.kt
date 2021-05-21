package com.example.aitama.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetTransaction
import java.text.SimpleDateFormat

@BindingAdapter("assetPriceSummed")
fun TextView.setAssetPriceSummed(item: AssetDto?) {
    // todo muss noch verglichen werden mit dem aktuellen preis
    item?.let { text = sumAssetPrice(item).toString() }

}

@BindingAdapter("assetAmountSummed")
fun TextView.setAssetAmountSummed(item: AssetDto?) {
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

@BindingAdapter("assetSymbol")
fun TextView.setAssetSymbol(item: AssetDto?) {

    item?.let {
        text = item.asset.symbol
    }
}

@BindingAdapter("assetPercentage")
fun TextView.setAssetPercentage(item: AssetDto?) {
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



