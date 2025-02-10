package com.sundeep.demo.currconv.data

import com.sundeep.demo.currconv.data.models.ConversionPairModel
import com.sundeep.demo.currconv.data.models.CurrencyModel
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getAllCurrencies(): Flow<List<CurrencyModel>>
    fun getConversions(currency: CurrencyModel): Flow<List<ConversionPairModel>>
    fun setDefaultCurrency(currency: CurrencyModel)
    fun getDefaultCurrency(): Flow<CurrencyModel?>
}