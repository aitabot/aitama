package com.example.aitama.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.aitama.dao.AitamaDatabase
import com.example.aitama.dao.AssetDao
import com.example.aitama.dao.AssetPriceDao
import com.example.aitama.dao.AssetTransactionDao
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetPrice
import com.example.aitama.dataclasses.AssetTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DataRepository(
    private val assetDao: AssetDao,
    private val assetTransactionDao: AssetTransactionDao,
    private val assetPriceDao: AssetPriceDao
) {

    suspend fun insertAsset(asset: Asset) {
        withContext(Dispatchers.IO) {
            assetDao.insert(asset)
        }
    }

    fun getAllAssets(): LiveData<List<Asset>> {
        return assetDao.getAllAssets()
    }

    suspend fun assetExists(symbol: String): Boolean {
        return withContext(Dispatchers.IO) {
            assetDao.assetExists(symbol = symbol)
        }
    }

    fun getAsset(symbol: String): LiveData<Asset> {
        return assetDao.getAssetBySymbol(symbol)
    }

    suspend fun updateAsset(asset: Asset) {
        withContext(Dispatchers.IO) {
            assetDao.update(asset = asset)
        }
    }

    suspend fun deleteAsset(asset: Asset) {
        withContext(Dispatchers.IO) {
            assetDao.delete(asset = asset)
            assetTransactionDao.deleteAllTransactionsForAsset(asset.symbol)
        }
    }

    suspend fun insertAssetTransaction(assetTransaction: AssetTransaction) {
        withContext(Dispatchers.IO) {
            assetTransactionDao.insert(assetTransaction)
        }
    }

    suspend fun updateAssetTransaction(assetTransaction: AssetTransaction) {
        withContext(Dispatchers.IO) {
            assetTransactionDao.update(assetTransaction)
        }
    }

    suspend fun deleteAssetTransaction(assetTransaction: AssetTransaction) {
        withContext(Dispatchers.IO) {
            assetTransactionDao.delete(assetTransaction)
        }
    }


    fun getAllAssetTransactions(): LiveData<List<AssetTransaction>> {
        return assetTransactionDao.getAllTransactions()
    }


    fun getAssetDto(symbol: String): LiveData<AssetDto> {
        return assetDao.getAssetDto(symbol)
    }

    fun getAllAssetDtos(): LiveData<List<AssetDto>> {
        return assetDao.getAllAssetDto()
    }


    fun getAllAssetTransactionsForAsset(symbol: String): LiveData<List<AssetTransaction>> {
        return assetTransactionDao.getAllTransactionsForAsset(symbol)
    }

    suspend fun insertAssetPrice(assetPrice: AssetPrice) {
        withContext(Dispatchers.IO) {
            assetPriceDao.insert(assetPrice)
        }
    }

    suspend fun insertAssetPrice(assetPrices: List<AssetPrice>) {
        withContext(Dispatchers.IO) {
            assetPriceDao.insert(assetPrices)
        }
    }


    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                val assetDao = AitamaDatabase.getInstance(application).assetDao
                val assetTransactionDao =
                    AitamaDatabase.getInstance(application).assetTransactionDao
                val assetPriceDao =
                    AitamaDatabase.getInstance(application).assetPriceDao
                instance ?: DataRepository(
                    assetDao,
                    assetTransactionDao,
                    assetPriceDao
                ).also { instance = it }
            }
    }


}