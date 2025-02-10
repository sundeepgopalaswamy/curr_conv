package com.sundeep.demo.currconv.data

import com.sundeep.demo.currconv.data.datasource.PreferencesDataSource
import com.sundeep.demo.currconv.data.datasource.RemoteDataSource
import com.sundeep.demo.currconv.data.models.ConversionDBModel
import com.sundeep.demo.currconv.data.models.ConversionPairModel
import com.sundeep.demo.currconv.data.models.CurrencyModel
import com.sundeep.demo.currconv.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultCurrencyRepository @Inject constructor(
    private val database: AppDatabase,
    private val dataSource: RemoteDataSource,
    private val preferencesDataSource: PreferencesDataSource
) :
    CurrencyRepository {
    private var currencies: List<CurrencyModel> = emptyList()
    private val allConversions: MutableMap<CurrencyModel, MutableMap<CurrencyModel, Double>> =
        HashMap()

    override fun getAllCurrencies(): Flow<List<CurrencyModel>> {
        return flow {
            if (currencies.isNotEmpty()) {
                emit(currencies)
                return@flow
            }
            currencies = getCurrenciesFromDB()
            emit(currencies)

            currencies = getCurrenciesFromNetwork()
            insertCurrenciesToDB(currencies)
            emit(currencies)
        }
    }

    private suspend fun insertCurrenciesToDB(allCurrencies: List<CurrencyModel>) {
        withContext(Dispatchers.IO) { database.currencyDao.insertAllCurrencies(allCurrencies) }
        Timber.i("Inserted currencies to DB")
    }

    private suspend fun getCurrenciesFromNetwork(): List<CurrencyModel> {
        Timber.i("Getting currencies from network")
        val unformattedCurrencies = withContext(Dispatchers.IO) { dataSource.getCurrencies() }
        val allCurrencies = mutableListOf<CurrencyModel>()

        unformattedCurrencies.forEach { rawData ->
            allCurrencies.add(CurrencyModel(rawData[0], rawData[1], rawData[2]))
        }
        return allCurrencies.sortedBy { it.name }
    }

    private suspend fun getCurrenciesFromDB(): List<CurrencyModel> {
        Timber.i("Getting currencies from DB")
        val currencyDao = database.currencyDao
        return withContext(Dispatchers.IO) { currencyDao.getAllCurrencies() }.sortedBy { it.name }
    }

    override fun getConversions(currency: CurrencyModel): Flow<List<ConversionPairModel>> {
        return flow {
            if (allConversions.containsKey(currency)) {
                emitConversionsFromMap(currency)
                return@flow
            }
            populateAllConversionsFromDB()
            if (allConversions.containsKey(currency)) {
                emitConversionsFromMap(currency)
            }
            fetchConversionsFromNetworkToDB()
            populateAllConversionsFromDB()
            if (allConversions.containsKey(currency)) {
                emitConversionsFromMap(currency)
            }
        }
    }

    override fun setDefaultCurrency(currency: CurrencyModel) {
        preferencesDataSource.setDefaultCurrency(currency.abb)
    }

    override fun getDefaultCurrency(): Flow<CurrencyModel?> {
        return flow {
            if (currencies.isNotEmpty()) {
                val defaultCurrency = preferencesDataSource.getDefaultCurrency()
                defaultCurrency?.let {
                    // TODO: Improve search logic to better than Linear.
                    currencies.forEach {
                        if (it.abb == defaultCurrency) {
                            emit(it)
                            return@flow
                        }
                    }
                }
            }
            emit(null)
        }
    }

    private suspend fun FlowCollector<List<ConversionPairModel>>.emitConversionsFromMap(
        currency: CurrencyModel
    ) {
        allConversions[currency]?.let { conversionMap ->
            Timber.i("Emitting ${conversionMap.size} conversions from Map for ${currency.name}")
            val conversions = mutableListOf<ConversionPairModel>()
            conversionMap.forEach { (toCur, rate) ->
                conversions.add(ConversionPairModel(toCur, rate))
            }
            emit(conversions.sortedBy { it.toCurrency.name })
        }
    }

    private suspend fun populateAllConversionsFromDB() {
        Timber.i("Populating all conversions from DB")
        val conversionsFromDB =
            withContext(Dispatchers.IO) { database.conversionDao.getAllConversions() }
        val currenciesMap = HashMap<String, CurrencyModel>()
        currencies.forEach {
            currenciesMap[it.abb] = it
        }

        conversionsFromDB.forEach { conversionDBModel ->
            val fromCurNullable = currenciesMap[conversionDBModel.fromCurrency]
            val toCurNullable = currenciesMap[conversionDBModel.toCurrency]
            val rate = conversionDBModel.rate
            fromCurNullable?.let { fromCur ->
                toCurNullable?.let { toCur ->
                    if (!allConversions.containsKey(fromCur)) {
                        allConversions[fromCur] = mutableMapOf()
                    }
                    allConversions[fromCur]?.let {
                        it[toCur] = rate
                    }
                    if (!allConversions.containsKey(toCur)) {
                        allConversions[toCur] = mutableMapOf()
                    }
                    allConversions[toCur]?.let {
                        it[fromCur] = 1 / rate
                    }
                }
            }
        }
    }

    private suspend fun fetchConversionsFromNetworkToDB() {
        Timber.i("Getting conversions from network")
        val conversionsFromNetwork = dataSource.getConversions()
        val conversionsToDB: MutableList<ConversionDBModel> = mutableListOf()

        conversionsFromNetwork.forEach { convList ->
            conversionsToDB.add(
                ConversionDBModel(
                    fromCurrency = convList[0],
                    toCurrency = convList[1],
                    rate = convList[2].toDouble()
                )
            )
        }
        withContext(Dispatchers.IO) { database.conversionDao.insertAllConversions(conversionsToDB) }
        Timber.i("Inserted ${conversionsToDB.size} conversions to DB")
    }
}