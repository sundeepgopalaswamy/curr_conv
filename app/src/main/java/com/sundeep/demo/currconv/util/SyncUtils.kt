package com.sundeep.demo.currconv.util

import com.sundeep.demo.currconv.data.datasource.RemoteDataSource
import com.sundeep.demo.currconv.data.models.ConversionDBModel
import com.sundeep.demo.currconv.data.models.CurrencyModel
import com.sundeep.demo.currconv.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


suspend fun fetchConversionsFromNetworkToDB(dataSource: RemoteDataSource, database: AppDatabase) {
    Timber.i("Getting conversions from network")
    val conversionsFromNetwork = dataSource.getConversions()
    val conversionsToDB: MutableList<ConversionDBModel> = mutableListOf()

    conversionsFromNetwork.forEach { convList ->
        conversionsToDB.add(
            ConversionDBModel(
                fromCurrency = convList[0], toCurrency = convList[1], rate = convList[2].toDouble()
            )
        )
    }
    withContext(Dispatchers.IO) { database.conversionDao.insertAllConversions(conversionsToDB) }
    Timber.i("Inserted ${conversionsToDB.size} conversions to DB")
}

suspend fun fetchCurrenciesFromNetworkToDB(
    dataSource: RemoteDataSource, database: AppDatabase
): List<CurrencyModel> {
    Timber.i("Getting currencies from network")
    val unformattedCurrencies = withContext(Dispatchers.IO) { dataSource.getCurrencies() }
    val allCurrencies = mutableListOf<CurrencyModel>()

    unformattedCurrencies.forEach { rawData ->
        allCurrencies.add(CurrencyModel(rawData[0], rawData[1], rawData[2]))
    }

    withContext(Dispatchers.IO) { database.currencyDao.insertAllCurrencies(allCurrencies) }
    Timber.i("Inserted currencies to DB")
    return allCurrencies.sortedBy { it.name }
}