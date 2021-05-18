package com.example.aitama.repositories

import androidx.lifecycle.LiveData
import com.example.aitama.dao.AssetDao
import com.example.aitama.dao.AssetTransactionDao
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DataRepository(
    private val assetDao: AssetDao,
    private val assetTransactionDao: AssetTransactionDao
) {

    suspend fun insertAsset(asset: Asset) {
        withContext(Dispatchers.IO) {
            assetDao.insert(asset)
        }
    }

    fun getAllAssets(): LiveData<List<Asset>> {
        return assetDao.getAllAssets()
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

    fun getAllAssetTransactionsForAsset(symbol: String): LiveData<List<AssetTransaction>> {
        return assetTransactionDao.getAllTransactionsForAsset(symbol)
    }


    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(assetDao: AssetDao, assetTransactionDao: AssetTransactionDao) =
            instance ?: synchronized(this) {
                instance ?: DataRepository(assetDao, assetTransactionDao).also { instance = it }
            }
    }
}