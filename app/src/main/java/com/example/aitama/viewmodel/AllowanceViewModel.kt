package com.example.aitama.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitama.repositories.DataRepository
import com.example.aitama.util.sumTransactions

class AllowanceViewModel(val dataRepository: DataRepository, val pref: SharedPreferences) :
    ViewModel() {

    val assetTransactions = dataRepository.getAllAssetTransactions()

    val amountEditText = MutableLiveData<String>()
    val allowanceSwitch = MutableLiveData<Boolean>()

    private val _currentAllowance = MutableLiveData<String>()
    val currentAllowance: LiveData<String>
        get() = _currentAllowance

    private val _remainingAllowance = MutableLiveData<String>()
    val remainingAllowance: LiveData<String>
        get() = _remainingAllowance


    init {
        refreshData()
    }


    private fun refreshData() {

        allowanceSwitch.value =
            pref.getBoolean(
                "allowance_switch",
                false
            )

        _currentAllowance.value =
            pref.getString("current_allowance", Double.POSITIVE_INFINITY.toString())

        updateRemainingAllowance()

    }

    fun updateRemainingAllowance() {

        assetTransactions.value?.let { transactions ->
            val transactionSum = sumTransactions(transactions)
            _remainingAllowance.value =
                currentAllowance.value?.toDouble()?.plus(transactionSum).toString()
        }

    }

    fun setAllowance() {

        with(pref.edit()) {

            this?.putString(
                "current_allowance",
                amountEditText.value
            )
            this?.putBoolean(
                "allowance_switch",
                allowanceSwitch.value!!
            )

            this?.commit()
        }

        amountEditText.value = ""
        refreshData()

    }

    fun resetAllowance() {

        with(pref.edit()) {

            this?.putString(
                "current_allowance",
                Double.POSITIVE_INFINITY.toString()
            )

            this?.putBoolean(
                "allowance_switch",
                allowanceSwitch.value!!
            )

            this?.commit()
        }
        refreshData()
    }


}