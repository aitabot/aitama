package com.example.aitama.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.aitama.dataclasses.AssetDetail

@BindingAdapter("assetPriceSummed")
fun TextView.setAssetPriceSummed(item: AssetDetail) {
    text = sumAssetPrice(item).toString()
}

@BindingAdapter("assetAmountSummed")
fun TextView.setAssetAmountSummed(item: AssetDetail) {
    text = sumAssetAmount(item).toString()
}

@BindingAdapter("assetName")
fun TextView.setAssetName(item: AssetDetail) {
    text = item.asset.name
}

@BindingAdapter("assetSymbol")
fun TextView.setAssetSymbol(item: AssetDetail) {
    text = item.asset.symbol
}

@BindingAdapter("assetPercentage")
fun TextView.setAssetPercentage(item: AssetDetail) {
    // todo fix this
    text = "100 %"
}
