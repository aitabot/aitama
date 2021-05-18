package com.example.aitama.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aitama.dataclasses.Asset
import com.example.aitama.dataclasses.AssetTransaction

@Database(entities = [Asset::class, AssetTransaction::class], version = 5, exportSchema = false)
abstract class AitamaDatabase : RoomDatabase() {

    abstract val assetDao: AssetDao
    abstract val assetTransactionDao: AssetTransactionDao

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
