package com.example.aitama.util

import androidx.room.TypeConverter
import com.example.aitama.dataclasses.Asset
import com.example.aitama.repositories.DataRepository
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }


}
