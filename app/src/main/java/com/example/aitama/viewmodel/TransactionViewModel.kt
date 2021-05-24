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
        transactionType: TransactionType?,
        assetAmount: Double?
    ) {

        viewModelScope.launch {


            transactionType?.let {


            }


            assetDto?.let {

                assetAmount?.let {

                    var amount = assetAmount.toFloat()
                    var price = (assetDto.assetPrices[0].price)

                    if (transactionType == TransactionType.SELL) {
                        amount *= (-1)
                    }


                    val transaction = AssetTransaction(
                        symbol = assetDto.asset.symbol,
                        amount = amount,
                        price = price,
                        date = Date.from(Instant.now())
                    )
                    dataRepository.insertAssetTransaction(transaction)

                }
            }
        }
    }


}