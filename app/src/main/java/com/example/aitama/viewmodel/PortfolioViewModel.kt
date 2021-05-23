package com.example.aitama.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetPrice
import com.example.aitama.dataclasses.AssetTransaction
import com.example.aitama.repositories.DataRepository
import com.example.aitama.rest.RequestQueueSingleton
import com.example.aitama.util.AssetType
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


class PortfolioViewModel(private val dataRepository: DataRepository) : ViewModel() {

    var assetDtos = dataRepository.getAllAssetDtos()

    init {

//        initializeValues()

    }

    fun initializeValues() {

        val apple = Asset("AAPL", "Apple Inc.", "12345", AssetType.STOCK)
        val google = Asset("GOOG", "Google", "1234", AssetType.STOCK)
        val btc = Asset("BTCUSD", "Bitcoin", "1234", AssetType.CRYPTO)

        val transactions = listOf(
            AssetTransaction(
                date = Date.from(Instant.now()),
                symbol = "AAPL",
                amount = 1.0f,
                price = 120.43f
            ),
            AssetTransaction(
                date = Date.from(Instant.now()),
                symbol = "GOOG",
                amount = 1.0f,
                price = 2100.0f
            ),
            AssetTransaction(
                date = Date.from(Instant.now()),
                symbol = "BTCUSD",
                amount = 1.0f,
                price = 33000.0f
            )
        )

        viewModelScope.launch {
            dataRepository.insertAsset(apple)
            dataRepository.insertAsset(google)
            dataRepository.insertAsset(btc)
            for (transaction in transactions) {
                for (i in 1..20) {
                    dataRepository.insertAssetTransaction(transaction)
                }
            }
        }
    }

    fun onAssetDetailClicked(symbol: String?) {

        _navigateToAssetDetail.value = symbol
    }

    fun onAssetDetailNavigated() {
        _navigateToAssetDetail.value = null
    }

    private val _navigateToAssetDetail = MutableLiveData<String?>()
    val navigateToAssetDetail
        get() = _navigateToAssetDetail


    fun verifyPriceDataIsCurrent(context: Context) {

        assetDtos.value?.forEach { assetDto ->
            val sortedPrices =
                assetDto.assetPrices.sortedByDescending { assetPrice -> assetPrice.date }

            if (sortedPrices.isEmpty()) {
                updatePriceData(
                    symbol = assetDto.asset.symbol,
                    date = Date.from(Instant.EPOCH),
                    context = context
                )
            } else if (sortedPrices[0].updatedDate < Date.from(
                    Instant.now().minus(24, ChronoUnit.HOURS)
                )
            ) {
                updatePriceData(
                    symbol = assetDto.asset.symbol,
                    date = sortedPrices[0].date,
                    context = context
                )
            }
        }
    }

    fun updatePriceData(symbol: String, date: Date, context: Context) {

        // todo, update the 'updated date' of the newest entry in ANY case
        // todo, get prices after a certain date, e.g. get only prices after the currently stored prices

        val requestQueue = RequestQueueSingleton.getInstance(context)
        val request = buildRequest(symbol, context)
        requestQueue.addToRequestQueue(request)

    }

    fun buildRequest(symbol: String, context: Context): JsonObjectRequest {

        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("http")
            .encodedAuthority("10.0.2.2:8000")
            .appendPath("asset")
            .appendPath(symbol)

        val url: String = builder.build().toString()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                viewModelScope.launch {

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

                    dataRepository.insertAssetPrice(priceList)

                }

                Toast.makeText(context, "${symbol} data retrieved", Toast.LENGTH_LONG).show()
            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }
        )
        return jsonObjectRequest
    }


}


