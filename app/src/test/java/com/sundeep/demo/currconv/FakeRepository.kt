package com.sundeep.demo.currconv

import com.sundeep.demo.currconv.data.CurrencyRepository
import com.sundeep.demo.currconv.data.models.ConversionPairModel
import com.sundeep.demo.currconv.data.models.CurrencyModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : CurrencyRepository {
    private val currencies = listOf(CurrencyModel("n1", "c1", "f1"), CurrencyModel("n2", "c2", "f2"))
    override fun getAllCurrencies(): Flow<List<CurrencyModel>> = flow {
        emit(currencies)
    }

    override fun getConversions(currency: CurrencyModel) : Flow<List<ConversionPairModel>> = flow {
        if (currency == currencies[0]) {
            emit(listOf(ConversionPairModel(currencies[1], 2.0)))
        } else if (currency == currencies[1]) {
            emit(listOf(ConversionPairModel(currencies[0], 0.5)))
        }
    }
}