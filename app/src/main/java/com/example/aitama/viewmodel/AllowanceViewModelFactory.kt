package com.example.aitama.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.repositories.DataRepository

class AllowanceViewModelFactory(
    private val dataRepository: DataRepository,
    private val preferences: SharedPreferences
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllowanceViewModel::class.java)) {
            return AllowanceViewModel(dataRepository = dataRepository, pref = preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}