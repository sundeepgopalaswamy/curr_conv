package com.sundeep.demo.currconv.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sundeep.demo.currconv.data.models.ConversionDBModel
import com.sundeep.demo.currconv.data.models.CurrencyModel

@Database(
    entities = [CurrencyModel::class, ConversionDBModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val currencyDao: CurrencyDao
    abstract val conversionDao: ConversionDao
}