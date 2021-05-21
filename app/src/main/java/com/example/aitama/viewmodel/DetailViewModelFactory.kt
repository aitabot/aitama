package com.example.aitama.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.repositories.DataRepository

class DetailViewModelFactory(
    private val dataRepository: DataRepository,
    private val symbol: String
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dataRepository = dataRepository, symbol = symbol) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}