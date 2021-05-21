package com.example.aitama.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.aitama.dataclasses.AssetDto

@BindingAdapter("assetPriceSummed")
fun TextView.setAssetPriceSummed(item: AssetDto?) {
    // todo muss noch verglichen werden mit dem aktuellen preis
    item?.let { text = sumAssetPrice(item).toString() }

}

@BindingAdapter("assetAmountSummed")
fun TextView.setAssetAmountSummed(item: AssetDto?) {
    item?.let {
        text = sumAssetAmount(item).toString()
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
    // todo fix this
    item?.let {
        text = "100 %"
    }
}
