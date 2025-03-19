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
        val CURRENCY2 = CurrencyModel("n3", "c3", "f3")
    }
    private val currencies = listOf(CURRENCY0, CURRENCY1, CURRENCY2)
    var savedFromCurrency: CurrencyModel? = null
    var savedToCurrency: CurrencyModel? = null

    override fun getAllCurrencies(): Flow<List<CurrencyModel>> = flow {
        emit(currencies)
    }

    override fun getConversions(currency: CurrencyModel) : Flow<List<ConversionPairModel>> = flow {
        if (currency == CURRENCY0) {
            emit(listOf(ConversionPairModel(CURRENCY1, 2.0), ConversionPairModel(CURRENCY2, 3.0)))
        } else if (currency == CURRENCY1) {
            emit(listOf(ConversionPairModel(CURRENCY0, 0.5), ConversionPairModel(CURRENCY2, 1.5)))
        }
    }

    override fun setFromCurrency(currency: CurrencyModel) {
        savedFromCurrency = currency
    }

    override fun getFromCurrency(): Flow<CurrencyModel?> {
        return flow {
            emit(savedFromCurrency)
        }
    }

    override fun setToCurrency(currency: CurrencyModel) {
        savedToCurrency = currency
    }

    override fun getToCurrency(): Flow<CurrencyModel?> {
        return flow {
            emit(savedToCurrency)
        }
    }
}