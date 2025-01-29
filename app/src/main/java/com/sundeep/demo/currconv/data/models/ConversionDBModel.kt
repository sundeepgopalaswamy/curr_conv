package com.sundeep.demo.currconv.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "conversion_table", primaryKeys = ["from_currency", "to_currency"])
data class ConversionDBModel(
    @ColumnInfo(name = "from_currency") val fromCurrency: String,
    @ColumnInfo(name = "to_currency") val toCurrency: String,
    @ColumnInfo(name = "rate") val rate: Double
) : Serializable
