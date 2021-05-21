package com.example.aitama.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetTransaction
import com.example.aitama.repositories.DataRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*


class PortfolioViewModel(private val dataRepository: DataRepository) : ViewModel() {


    var assetDetails = dataRepository.getAllAssetDetails()

    fun initializeValues() {

        val apple = Asset("AAPL", "Apple Inc.", "12345", "Stock")
        val google = Asset("GOOG", "Google", "1234", "Stock")

        val transactions = listOf(
            AssetTransaction(
                date = Date.from(Instant.now()),
                symbol = "AAPL",
                amount = 1.0f,
                price = 2000.0f
            ),
            AssetTransaction(
                date = Date.from(Instant.now()),
                symbol = "GOOG",
                amount = 1.0f,
                price = 2000.0f
            )
        )

        viewModelScope.launch {
            dataRepository.insertAsset(apple)
            dataRepository.insertAsset(google)
            for (transaction in transactions) {
                for (i in 1..20) {
                    dataRepository.insertAssetTransaction(transaction)
                }
            }
        }
    }

    fun onAssetDetailClicked(symbol: String) {

        _navigateToAssetDetail.value = symbol
    }

    fun onAssetDetailNavigated() {
        _navigateToAssetDetail.value = null
    }

    private val _navigateToAssetDetail = MutableLiveData<String>()
    val navigateToAssetDetail
        get() = _navigateToAssetDetail


}


