package com.example.aitama.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetTransaction
import com.example.aitama.repositories.DataRepository

class TransactionViewModelFactory(
    private val dataRepository: DataRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(dataRepository = dataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}