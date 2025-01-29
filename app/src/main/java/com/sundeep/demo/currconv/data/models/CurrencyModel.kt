package com.sundeep.demo.currconv.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "currency_table")
data class CurrencyModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "currency_name") val name: String,
    @ColumnInfo(name = "currency_abb") val abb: String,
    @ColumnInfo(name = "currency_flag_url") val flagUrl: String
) : Serializable