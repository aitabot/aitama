package com.example.aitama.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetDetails
import com.example.aitama.dataclasses.AssetTransaction
import com.example.aitama.repositories.DataRepository
import kotlinx.coroutines.launch
import java.util.stream.Collectors


class PortfolioViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val assets = dataRepository.getAllAssets()
    private val assetTransactions = dataRepository.getAllAssetTransactions()
    var combinedLiveData = MediatorLiveData<List<AssetDetails>>()

    init {
        combinedLiveData.addSource(
            assets
        ) { combinedLiveData.value = combine(assets, assetTransactions) }

        combinedLiveData.addSource(
            assetTransactions
        ) { combinedLiveData.value = combine(assets, assetTransactions) }
    }

    // TODO: Implement the ViewModel

//    fun initializeValues() {
//
//        val apple = Asset("AAPL", "Apple Inc.", "12345", "Stock")
//        val google = Asset("GOOG", "Google", "1234", "Stock")
//
//        val transactions = listOf(
//            AssetTransaction(symbol = "AAPL", amount = 1.0f, price = 2000.0f),
//            AssetTransaction(symbol = "AAPL", amount = 1.0f, price = 2000.0f),
//            AssetTransaction(symbol = "AAPL", amount = 1.0f, price = 2000.0f),
//            AssetTransaction(symbol = "AAPL", amount = 1.0f, price = 2000.0f),
//            AssetTransaction(symbol = "GOOG", amount = 1.0f, price = 2000.0f),
//            AssetTransaction(symbol = "GOOG", amount = 1.0f, price = 2000.0f),
//            AssetTransaction(symbol = "GOOG", amount = 1.0f, price = 2000.0f),
//            AssetTransaction(symbol = "GOOG", amount = 1.0f, price = 2000.0f),
//        )
//
//        viewModelScope.launch {
//            dataRepository.insertAsset(apple)
//            dataRepository.insertAsset(google)
//            for (transaction in transactions) {
//                dataRepository.insertAssetTransaction(transaction)
//            }
//        }
//    }

    fun combine(
        assets: LiveData<List<Asset>>,
        assetTransactions: LiveData<List<AssetTransaction>>
    ): MutableList<AssetDetails> {

        val result = mutableListOf<AssetDetails>()

        assets.value?.forEach { asset ->

            result.add(
                AssetDetails(
                    asset,
                    assetTransactions.value?.stream()
                        ?.filter { t -> t.symbol == asset.symbol }
                        ?.collect(
                            Collectors.toList()
                        )
                )
            )

        }

        return result
    }


}


