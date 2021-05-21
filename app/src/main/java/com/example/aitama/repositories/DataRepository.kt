package com.example.aitama.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.aitama.dao.AitamaDatabase
import com.example.aitama.dao.AssetDao
import com.example.aitama.dao.AssetTransactionDao
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.dataclasses.AssetTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.stream.Collectors


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

    fun getAsset(symbol: String): LiveData<List<Asset>> {
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


    fun getAllAssetTransactionsForAsset(symbol: String): LiveData<List<AssetTransaction>> {
        return assetTransactionDao.getAllTransactionsForAsset(symbol)
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                val assetDao = AitamaDatabase.getInstance(application).assetDao
                val assetTransactionDao = AitamaDatabase.getInstance(application).assetTransactionDao
                instance ?: DataRepository(assetDao, assetTransactionDao).also { instance = it }
            }
    }

    fun getAssetDetailsForAsset(symbol: String): MediatorLiveData<List<AssetDto>> {

        val assets = getAsset(symbol)
        val assetTransactions = getAllAssetTransactionsForAsset(symbol)
        val combinedLiveData = MediatorLiveData<List<AssetDto>>()

        // todo kill duplicate code
        combinedLiveData.addSource(
            assets
        ) { combinedLiveData.value = combineDataSets(assets, assetTransactions) }

        combinedLiveData.addSource(
            assetTransactions
        ) { combinedLiveData.value = combineDataSets(assets, assetTransactions) }

        return combinedLiveData

    }


    fun getAllAssetDetails(): MediatorLiveData<List<AssetDto>> {


        val assets = getAllAssets()
        val assetTransactions = getAllAssetTransactions()
        val combinedLiveData = MediatorLiveData<List<AssetDto>>()

        // todo kill duplicate code
        combinedLiveData.addSource(
            assets
        ) { combinedLiveData.value = combineDataSets(assets, assetTransactions) }

        combinedLiveData.addSource(
            assetTransactions
        ) { combinedLiveData.value = combineDataSets(assets, assetTransactions) }

        return combinedLiveData

    }


    private fun combineDataSets(
        assets: LiveData<List<Asset>>,
        assetTransactions: LiveData<List<AssetTransaction>>
    ): MutableList<AssetDto> {

        val result = mutableListOf<AssetDto>()

        assets.value?.forEach { asset ->

            result.add(
                AssetDto(
                    asset,
                    assetTransactions.value?.stream()
                        ?.filter { t -> t.symbol == asset.symbol }
                        ?.collect(
                            Collectors.toList()
                        )
                )
            )

        }

        return result
    }


}