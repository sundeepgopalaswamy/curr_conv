package com.sundeep.demo.currconv

import com.sundeep.demo.currconv.data.CurrencyRepository
import com.sundeep.demo.currconv.data.models.ConversionPairModel
import com.sundeep.demo.currconv.data.models.CurrencyModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : CurrencyRepository {
    companion object {
        val CURRENCY0 = CurrencyModel("n1", "c1", "f1")
        val CURRENCY1 = CurrencyModel("n2", "c2", "f2")
    }
    private val currencies = listOf(CURRENCY0, CURRENCY1)
    var savedDefaultCurrency: CurrencyModel? = null

    override fun getAllCurrencies(): Flow<List<CurrencyModel>> = flow {
        emit(currencies)
    }

    override fun getConversions(currency: CurrencyModel) : Flow<List<ConversionPairModel>> = flow {
        if (currency == CURRENCY0) {
            emit(listOf(ConversionPairModel(CURRENCY1, 2.0)))
        } else if (currency == CURRENCY1) {
            emit(listOf(ConversionPairModel(CURRENCY0, 0.5)))
        }
    }

    override fun setDefaultCurrency(currency: CurrencyModel) {
        savedDefaultCurrency = currency
    }

    override fun getDefaultCurrency(): Flow<CurrencyModel?> {
        return flow {
            emit(savedDefaultCurrency)
        }
    }
}