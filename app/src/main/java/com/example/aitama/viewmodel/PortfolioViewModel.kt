package com.example.aitama.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetPrice
import com.example.aitama.dataclasses.AssetTransaction
import com.example.aitama.repositories.DataRepository
import com.example.aitama.util.AssetType
import com.example.aitama.util.TransactionType
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*


class PortfolioViewModel(private val dataRepository: DataRepository) : ViewModel() {

    val assetDtos = dataRepository.getAllAssetDtos()

    init {
//        initializeValues()
    }

    fun checkPriceActuality(context: Context) {

        viewModelScope.launch {
            assetDtos.value?.forEach {
                it.let {
                    com.example.aitama.util.verifyPriceDataIsCurrent(
                        assetDto = it,
                        context = context,
                        insertFunction = ::savePrices
                    )
                }
            }
        }
    }

    private fun savePrices(assetPrices: List<AssetPrice>) {
        viewModelScope.launch {
            dataRepository.insertAssetPrices(assetPrices)
        }
    }


    private fun initializeValues() {

        val apple = Asset("AAPL", "Apple Inc.", "12345", AssetType.STOCK)
        val google = Asset("GOOG", "Google", "1234", AssetType.STOCK)
        val btc = Asset("BTCUSD", "Bitcoin", "1234", AssetType.CRYPTO)

        val transactions = listOf(
            AssetTransaction(
                date = Date.from(Instant.now()),
                symbol = "AAPL",
                amount = 15.0f,
                price = 120.43f,
                transactionType = TransactionType.BUY
            ),
            AssetTransaction(
                date = Date.from(Instant.now()),
                symbol = "GOOG",
                amount = 1.0f,
                price = 2100.0f,
                transactionType = TransactionType.BUY
            ),
            AssetTransaction(
                date = Date.from(Instant.now()),
                symbol = "BTCUSD",
                amount = 0.07f,
                price = 33000.0f,
                transactionType = TransactionType.BUY
            )
        )

        viewModelScope.launch {
            dataRepository.insertAsset(apple)
            dataRepository.insertAsset(google)
            dataRepository.insertAsset(btc)
            for (transaction in transactions) {
                for (i in 1..2) {
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

}


