package com.example.aitama.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetPrice
import com.example.aitama.dataclasses.AssetTransaction
import com.example.aitama.repositories.DataRepository
import com.example.aitama.util.AssetType
import com.example.aitama.util.TransactionType
import com.example.aitama.util.sumTransactions
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

class TransactionViewModel(
    private val dataRepository: DataRepository,
    private val pref: SharedPreferences,
    val symbol: String,
    val name: String,
    private val assetType: AssetType,
    val transactionType: TransactionType

) : ViewModel() {

    val transactions = dataRepository.getAllAssetTransactions()
    val transactionAmount = MutableLiveData<String>()

    private val _transactionPrice = MutableLiveData<String>()
    val transactionPrice: LiveData<String>
        get() = _transactionPrice

    val assetDto: LiveData<AssetDto> = dataRepository.getLiveDataAssetDto(symbol = symbol)

    fun checkPriceActuality(context: Context) {

        viewModelScope.launch {
            assetDto.value?.let {
                com.example.aitama.util.verifyPriceDataIsCurrent(
                    assetDto = it,
                    context = context,
                    insertFunction = ::savePrices
                )
            }
        }
    }


    private fun savePrices(assetPrices: List<AssetPrice>) {
        viewModelScope.launch {
            dataRepository.insertAssetPrices(assetPrices)
        }
    }


    private val _currentAllowance = MutableLiveData<String>()
    private val currentAllowance: LiveData<String>
        get() = _currentAllowance

    private val _remainingAllowance = MutableLiveData<String>()
    val remainingAllowance: LiveData<String>
        get() = _remainingAllowance

    private val _remainingAfterTransaction = MutableLiveData<String>()
    val remainingAfterTransaction: LiveData<String>
        get() = _remainingAfterTransaction

    init {
        viewModelScope.launch {
            dataRepository.conditionallyCreateAsset(symbol, name, assetType)
        }
        transactionAmount.value = "0"
        loadAllowance()
    }


    fun loadAllowance() {

        transactions.value?.let {
            val transactionSum = sumTransactions(it)
            _currentAllowance.value =
                pref.getString("current_allowance", Double.POSITIVE_INFINITY.toString())
            _remainingAllowance.value =
                currentAllowance.value?.toDouble()?.plus(transactionSum).toString()
        }

    }


    fun confirmTransaction(
        assetDto: AssetDto?,
        transactionType: TransactionType,
        assetAmount: Double?
    ) {

        assetDto?.let {

            assetAmount?.let {

                var transactionAmount = assetAmount.toFloat()
                val price = (assetDto.assetPrices[0].price)

                if (transactionType == TransactionType.SELL) {
                    markTransactionsAsSold(assetDto, transactionAmount)
                    transactionAmount *= (-1)
                }

                val transaction = AssetTransaction(
                    symbol = assetDto.asset.symbol,
                    amount = transactionAmount,
                    price = price,
                    date = Date.from(Instant.now()),
                    transactionType = transactionType
                )
                viewModelScope.launch {
                    dataRepository.insertAssetTransaction(transaction)
                }


            }

        }
    }

/* This function will retrieve the oldest 'unsold' buy transactions and will subtract the amount to be sold from these transactions
* It will then continue to update the transactions in the database.
* This serves the purpose to allow checking of the invested amount of the currently held assets. */

    private fun markTransactionsAsSold(assetDto: AssetDto, transactionAmount: Float) {

        var remainingAmount = transactionAmount
        val unsoldAssets = mutableListOf<AssetTransaction>()

        assetDto.assetTransactions
            .sortedBy { assetTransaction -> assetTransaction.transactionId }
            .filter { assetTransaction -> assetTransaction.amountSold != assetTransaction.amount && assetTransaction.transactionType == TransactionType.BUY }
            .toCollection(unsoldAssets)

        for (unsoldAsset in unsoldAssets) {

            if (remainingAmount != 0.0f) {

                val availableAmount = unsoldAsset.amount - unsoldAsset.amountSold

                if (remainingAmount <= availableAmount) {

                    unsoldAsset.amountSold += remainingAmount
                    remainingAmount -= remainingAmount
                    viewModelScope.launch {
                        dataRepository.updateAssetTransaction(unsoldAsset)
                    }
                    break

                } else if (remainingAmount > availableAmount) {

                    remainingAmount -= availableAmount
                    unsoldAsset.amountSold += availableAmount
                    viewModelScope.launch {
                        dataRepository.updateAssetTransaction(unsoldAsset)
                    }

                }
            }
        }

    }

    fun updateTransactionPrice() {

        assetDto.value?.assetPrices?.let {
            if (it.isNotEmpty()) {
                val currentPrice = it[0].price
                transactionAmount.value?.toDoubleOrNull()?.let { amount ->
                    currentPrice.let {
                        _transactionPrice.value = (currentPrice * amount).toString()
                    }
                }
            }
        }
    }

    fun updateRemainingAllowanceAfterTransaction(type: TransactionType) {
        if (type == TransactionType.SELL) {
            transactionPrice.value?.let {
                _remainingAfterTransaction.value = (remainingAllowance.value?.toDouble()
                    ?.plus(it.toDouble())).toString()
            }

        } else if (type == TransactionType.BUY) {
            transactionPrice.value?.let {
                _remainingAfterTransaction.value = (remainingAllowance.value?.toDouble()
                    ?.minus(it.toDouble())).toString()
            }
        }
    }


}