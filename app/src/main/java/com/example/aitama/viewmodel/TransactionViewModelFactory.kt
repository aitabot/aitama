package com.example.aitama.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.repositories.DataRepository

class TransactionViewModelFactory(
    private val dataRepository: DataRepository,
    private val preferences: SharedPreferences
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(dataRepository = dataRepository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}