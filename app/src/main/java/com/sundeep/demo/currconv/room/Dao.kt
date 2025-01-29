package com.sundeep.demo.currconv.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sundeep.demo.currconv.data.models.ConversionDBModel
import com.sundeep.demo.currconv.data.models.CurrencyModel

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency_table")
    fun getAllCurrencies(): List<CurrencyModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCurrencies(data: List<CurrencyModel>)
}

@Dao
interface ConversionDao {
    @Query("SELECT * FROM conversion_table")
    fun getAllConversions(): List<ConversionDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllConversions(data: List<ConversionDBModel>)
}