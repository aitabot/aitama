package com.example.aitama.viewmodel

import androidx.lifecycle.ViewModel
import com.example.aitama.repositories.DataRepository

class DetailViewModel(private val dataRepository: DataRepository, symbol: String) : ViewModel() {

    val transactionList = dataRepository.getAllAssetTransactionsForAsset(symbol)
    val asset = dataRepository.getAsset(symbol)

}