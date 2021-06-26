package com.example.aitama.util

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.example.aitama.R
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetTransaction
import java.text.NumberFormat
import java.text.SimpleDateFormat


@BindingAdapter("totalAssetAmount")
fun TextView.setTotalAssetAmount(item: List<AssetDto>?) {
    item?.let {

        text = resources.getString(R.string.assets, it.size.toString())
    }
}

@BindingAdapter("totalStockAmount")
fun TextView.setTotalStockAmount(item: List<AssetDto>?) {
    item?.let {
        val amount = it.filter { assetDto -> assetDto.asset.type == AssetType.STOCK }.count()
        text = resources.getString(R.string.stocks, amount.toString())
    }
}

@BindingAdapter("totalCryptoAmount")
fun TextView.setTotalCryptoAmount(item: List<AssetDto>?) {
    item?.let {
        val amount = it.filter { assetDto -> assetDto.asset.type == AssetType.CRYPTO }.count()
        text = resources.getString(R.string.cryptos, amount.toString())
    }
}

@BindingAdapter("totalCryptoValue")
fun TextView.setTotalCryptoValue(item: List<AssetDto>?) {

    item?.let {
        val sum = it.filter { dto -> dto.asset.type == AssetType.CRYPTO }
            .sumOf { dto ->
                sumAssetValue(dto)
            }
        text = formatDollar(sum)
    }
}

@BindingAdapter("totalStockValue")
fun TextView.setTotalStockValue(item: List<AssetDto>?) {

    item?.let {
        val sum = it.filter { dto -> dto.asset.type == AssetType.STOCK }
            .sumOf { dto ->
                sumAssetValue(dto)
            }
        text = formatDollar(sum)
    }

}


@BindingAdapter("totalValue")
fun TextView.setTotalPrice(item: List<AssetDto>?) {

    item?.let {
        val sum = it.sumOf { dto ->
            sumAssetValue(dto)
        }
        text = formatDollar(sum)
    }
}


@BindingAdapter("assetValueSummed")
fun TextView.setAssetValueSummed(item: AssetDto?) {
    item?.let {
        val value = sumAssetValue(it)
        text = formatDollar(value)
    }
}

@BindingAdapter("assetPriceSummed")
fun TextView.setAssetPriceSummed(item: AssetDto?) {
    item?.let {
        val price = sumAssetPrice(it)
        text = formatDollar(price)
    }
}

@BindingAdapter("currentPriceLabel")
fun TextView.setCurrentPriceLabel(item: AssetDto?) {
    item?.let {
        val date = it.assetPrices.sortedByDescending { assetPrice -> assetPrice.date }[0].date
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val formattedDate = simpleDateFormat.format(date)

        text = resources.getString(
            R.string.current_price,
            formattedDate
        )
    }
}

@BindingAdapter("latestPrice")
fun TextView.setLatestPrice(item: AssetDto?) {
    item?.let {
        val price = it.assetPrices.sortedByDescending { assetPrice -> assetPrice.date }[0].price
        text = formatDollar(price.toDouble())
    }
}


@BindingAdapter("averageAssetPurchasePrice")
fun TextView.setAverageAssetPurchasePrice(item: AssetDto?) {
    item?.let {
        val totalPrice = sumAssetPrice(it)
        val totalNumberOfPurchases = sumAmountOfPurchasedAssets(item.assetTransactions)
        text = formatDollar(totalPrice / totalNumberOfPurchases)
    }
}


@BindingAdapter("dollarFormattedDoubleString")
fun TextView.setAssetPriceSummed(item: String?) {
    item?.let {
        text = formatDollar(item.toDoubleOrNull())
    }
}

@BindingAdapter("assetAmountSummed")
fun TextView.setAssetAmountSummed(item: AssetDto?) {
    item?.let {

        val format = NumberFormat.getInstance()
        format.maximumFractionDigits = 5
        val string = format.format(sumAssetAmount(item.assetTransactions))

        text = resources.getString(
            R.string.asset_amount,
            string
        )
    }
}


@BindingAdapter("assetName")
fun TextView.setAssetName(item: AssetDto?) {
    item?.let {
//        text = "${item.asset.name} (${item.asset.symbol})"
        text = resources.getString(R.string.asset_name, item.asset.name, item.asset.symbol)
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


@BindingAdapter("assetPerformancePercentage")
fun TextView.setAssetPerformancePercentage(item: AssetDto?) {
    item?.let {

        val percentage = calculatePerformancePercentage(item)
        if (percentage >= 0) {
            setTextColor(resources.getColor(R.color.green, null))
        } else {
            setTextColor(resources.getColor(R.color.red, null))
        }
        text = formatPercentage(percentage)

    }
}

@BindingAdapter("assetPerformancePercentageDay")
fun TextView.setAssetPerformancePercentageDay(item: AssetDto?) {
    item?.let {
        val percentage = calculatePerformancePercentage(item, position = 1)
        text = formatPercentage(percentage)
    }
}

@BindingAdapter("assetPerformancePercentageWeek")
fun TextView.setAssetPerformancePercentageWeek(item: AssetDto?) {
    item?.let {
        val percentage = calculatePerformancePercentage(item, position = 5)
        text = formatPercentage(percentage)
    }
}

@BindingAdapter("assetPerformancePercentageMonth")
fun TextView.setAssetPerformancePercentageMonth(item: AssetDto?) {
    item?.let {
        val percentage = calculatePerformancePercentage(item, position = 20)
        text = formatPercentage(percentage)
    }
}

@BindingAdapter("assetPerformancePercentageDay")
fun TextView.setAssetPerformancePercentageDay(item: List<AssetDto>?) {
    item?.let {
        text = formatPercentage(calculatePerformancePercentage(it, position = 1))
    }
}

@BindingAdapter("assetPerformancePercentageWeek")
fun TextView.setAssetPerformancePercentageWeek(item: List<AssetDto>?) {
    item?.let {
        text = formatPercentage(calculatePerformancePercentage(it, position = 5))
    }
}

@BindingAdapter("assetPerformancePercentageMonth")
fun TextView.setAssetPerformancePercentageMonth(item: List<AssetDto>?) {
    item?.let {
        text = formatPercentage(calculatePerformancePercentage(it, position = 20))
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

        text = resources.getString(R.string.asset_amount, item.amount.toString())
    }
}

@BindingAdapter("transactionPrice")
fun TextView.setTransactionPrice(item: AssetTransaction?) {
    item?.let {
        // todo include currency in data set?
        text = formatDollar((item.price).toDouble())
    }
}


@BindingAdapter("transactionValueTotal")
fun TextView.setTransactionValueTotal(item: AssetTransaction?) {
    item?.let {
        // todo include currency in data set?
        text = formatDollar((item.price * item.amount * (-1)).toDouble())
    }
}


@InverseBindingAdapter(attribute = "android:text")
fun getText(editText: EditText): String {
    return editText.text.toString()
}


@BindingAdapter("android:text")
fun setText(view: TextView, type: TransactionType?) {

    type?.let {
        view.text = type.toString()
    }
}








