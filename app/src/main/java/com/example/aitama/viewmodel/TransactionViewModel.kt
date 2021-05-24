package com.example.aitama.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetTransaction
import com.example.aitama.repositories.DataRepository
import com.example.aitama.util.TransactionType
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

class TransactionViewModel(private val dataRepository: DataRepository) : ViewModel() {


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


}