package com.example.aitama.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetPrice
import com.example.aitama.dataclasses.AssetTransaction
import com.example.aitama.util.Converters

@Database(
    entities = [Asset::class, AssetTransaction::class, AssetPrice::class],
    version = 35,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AitamaDatabase : RoomDatabase() {

    abstract val assetDao: AssetDao
    abstract val assetTransactionDao: AssetTransactionDao
    abstract val assetPriceDao: AssetPriceDao

    companion object {

        @Volatile
        private var INSTANCE: AitamaDatabase? = null

        fun getInstance(context: Context): AitamaDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AitamaDatabase::class.java,
                        "aitama_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
