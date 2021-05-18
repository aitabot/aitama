package com.example.aitama.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.repositories.DataRepository

class PortfolioViewModelFactory(private val dataRepository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortfolioViewModel::class.java)) {
            return PortfolioViewModel(dataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}