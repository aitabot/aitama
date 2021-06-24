package com.example.aitama.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.repositories.DataRepository
import com.example.aitama.util.AssetType
import com.example.aitama.util.TransactionType

class TransactionViewModelFactory(
    private val dataRepository: DataRepository,
    private val preferences: SharedPreferences,
    private val symbol: String,
    private val name: String,
    private val assetType: AssetType,
    private val transactionType: TransactionType
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(
                dataRepository = dataRepository,
                pref = preferences,
                symbol = symbol,
                name = name,
                assetType = assetType,
                transactionType = transactionType
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}