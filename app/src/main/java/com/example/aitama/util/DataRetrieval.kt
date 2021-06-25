package com.example.aitama.util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetPrice
import com.example.aitama.rest.RequestQueueSingleton
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

fun verifyPriceDataIsCurrent(
    assetDto: AssetDto,
    context: Context,
    insertFunction: (List<AssetPrice>) -> Unit
) {

    val sortedPrices =
        assetDto.assetPrices.sortedByDescending { assetPrice -> assetPrice.date }

    if (sortedPrices.isEmpty()) {
        updatePriceData(
            symbol = assetDto.asset.symbol,
            date = Date.from(Instant.EPOCH),
            context = context,
            insertFunction = insertFunction
        )
    } else if (sortedPrices[0].updatedDate < Date.from(
            Instant.now().minus(24, ChronoUnit.HOURS)
        )
    ) {
        updatePriceData(
            symbol = assetDto.asset.symbol,
            date = sortedPrices[0].date,
            context = context,
            insertFunction = insertFunction
        )
    }

}

fun updatePriceData(
    symbol: String,
    date: Date,
    context: Context,
    insertFunction: (List<AssetPrice>) -> Unit
) {

    // todo, update the 'updated date' of the newest entry in ANY case
    // todo, get prices after a certain date, e.g. get only prices after the currently stored prices

    val requestQueue = RequestQueueSingleton.getInstance(context)
    val request = buildRequest(symbol, context, insertFunction)
    requestQueue.addToRequestQueue(request)

}

fun buildRequest(
    symbol: String,
    context: Context,
    insertFunction: (List<AssetPrice>) -> Unit
): JsonObjectRequest {

    val builder: Uri.Builder = Uri.Builder()
    builder.scheme("https")
        .encodedAuthority("aitamonolith.qtq.at")
        .appendPath("asset")
        .appendPath(symbol)

    val url: String = builder.build().toString()
    return createJsonObjectRequest(url, symbol, context, insertFunction)

}

fun createJsonObjectRequest(
    url: String,
    symbol: String,
    context: Context,
    insertFunction: (List<AssetPrice>) -> Unit
): JsonObjectRequest {

    return JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            insertFunction(extractPrices(symbol, response))
            Toast.makeText(context, "$symbol data retrieved", Toast.LENGTH_LONG).show()
        },
        { error ->
            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
        }
    )
}

fun extractPrices(
    symbol: String,
    response: JSONObject,
): List<AssetPrice> {

    val priceList = mutableListOf<AssetPrice>()

    val prices = response.get("asset_price") as JSONArray
    for (i in 0 until prices.length()) {

        val item = prices[i] as JSONObject
        val dateString = item["dt"] as String
        val date = SimpleDateFormat("yyyy-MM-dd").parse(dateString)
        val close = (item["close"] as Double).toFloat()
        priceList.add(
            AssetPrice(
                date = date,
                symbol = symbol,
                price = close,
                updatedDate = Date.from(Instant.now())
            )
        )
    }
    return priceList
}

