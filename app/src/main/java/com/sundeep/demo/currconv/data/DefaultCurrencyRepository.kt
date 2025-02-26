package com.sundeep.demo.currconv.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sundeep.demo.currconv.data.datasource.PreferencesDataSource
import com.sundeep.demo.currconv.data.datasource.RemoteDataSource
import com.sundeep.demo.currconv.data.models.ConversionPairModel
import com.sundeep.demo.currconv.data.models.CurrencyModel
import com.sundeep.demo.currconv.room.AppDatabase
import com.sundeep.demo.currconv.util.fetchConversionsFromNetworkToDB
import com.sundeep.demo.currconv.util.fetchCurrenciesFromNetworkToDB
import com.sundeep.demo.currconv.workers.OfflineSyncWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DefaultCurrencyRepository @Inject constructor(
    context: Context,
    private val database: AppDatabase,
    private val dataSource: RemoteDataSource,
    private val preferencesDataSource: PreferencesDataSource
) : CurrencyRepository {
    companion object {
        // Used 10sec for testing. In real world this probably would be 5 minutes
        const val CACHING_TIME = 10_000L
        const val OFFLINE_SYNC_WORK_NAME = "offline_sync_work"
    }

    private var currencies: List<CurrencyModel> = emptyList()
    private val allConversions: MutableMap<CurrencyModel, MutableMap<CurrencyModel, Double>> =
        HashMap()
    private val workManager = WorkManager.getInstance(context)
    private val cache = Cache()

    override fun getAllCurrencies(): Flow<List<CurrencyModel>> {
        return flow {
            if (currencies.isEmpty()) {
                currencies = getCurrenciesFromDB()
            }
            emit(currencies)
            if (cache.isNotStaleCurrencyCache()) {
                return@flow
            }

            currencies = fetchCurrenciesFromNetworkToDB(dataSource, database)
            cache.updateLastFetchedCurrencyTime()
            emit(currencies)
        }
    }

    private suspend fun getCurrenciesFromDB(): List<CurrencyModel> {
        Timber.i("Getting currencies from DB")
        val currencyDao = database.currencyDao
        return withContext(Dispatchers.IO) { currencyDao.getAllCurrencies() }.sortedBy { it.name }
    }

    override fun getConversions(currency: CurrencyModel): Flow<List<ConversionPairModel>> {
        return flow {
            if (!allConversions.containsKey(currency)) {
                populateAllConversionsFromDB()
            }
            emitConversionsFromMap(currency)
            if (cache.isNotStaleConversionsCache()) {
                return@flow
            }

            fetchConversionsFromNetworkToDB(dataSource, database)
            cache.updateLastFetchedConversionTime()
            populateAllConversionsFromDB()
            emitConversionsFromMap(currency)
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

    private fun scheduleOfflineSync() {
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.UNMETERED).build()
        val workRequest =
            PeriodicWorkRequestBuilder<OfflineSyncWorker>(3, TimeUnit.HOURS).setConstraints(
                constraints
            ).build()
        workManager.enqueueUniquePeriodicWork(
            OFFLINE_SYNC_WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, workRequest
        )
    }

    private inner class Cache {
        private var lastFetchedCurrencyInMs = 0L
        private var lastFetchedConversionsInMs = 0L

        fun isNotStaleCurrencyCache(): Boolean {
            return Instant.now().toEpochMilli() - lastFetchedCurrencyInMs < CACHING_TIME
        }

        fun isNotStaleConversionsCache(): Boolean {
            return Instant.now().toEpochMilli() - lastFetchedConversionsInMs < CACHING_TIME
        }

        fun updateLastFetchedCurrencyTime() {
            lastFetchedCurrencyInMs = Instant.now().toEpochMilli()
        }

        fun updateLastFetchedConversionTime() {
            lastFetchedConversionsInMs = Instant.now().toEpochMilli()
            scheduleOfflineSync()
        }
    }
}