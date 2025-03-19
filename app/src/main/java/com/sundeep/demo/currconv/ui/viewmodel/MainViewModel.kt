package com.sundeep.demo.currconv.ui.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sundeep.demo.currconv.data.CurrencyRepository
import com.sundeep.demo.currconv.data.models.ConversionPairModel
import com.sundeep.demo.currconv.data.models.CurrencyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Currency
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(@get:VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) var repository: CurrencyRepository) :
    ViewModel() {
    private val _allCur = MutableStateFlow<List<CurrencyModel>>(emptyList())
    private val _conversions = MutableStateFlow<List<ConversionPairModel>>(emptyList())
    private var _fromCurrency = MutableStateFlow<CurrencyModel?>(null)
    private var _toCurrency = MutableStateFlow<CurrencyModel?>(null)
    private val _dataloaded = MutableStateFlow(false)
    private var curConverterValue = 1.0
    val allCurrencies: StateFlow<List<CurrencyModel>> get() = _allCur
    val conversions: StateFlow<List<ConversionPairModel>> get() = _conversions
    val fromCurrency: StateFlow<CurrencyModel?> get() = _fromCurrency
    val toCurrency: StateFlow<CurrencyModel?> get() = _toCurrency
    val dataloaded: StateFlow<Boolean> get() = _dataloaded
    val fromCurrencyAmount = MutableStateFlow(0.0)
    val toCurrencyAmount = MutableStateFlow(0.0)


    init {
        viewModelScope.launch {
            repository.getAllCurrencies().collect { data ->
                Timber.i("Populated with ${data.size} currencies")
                if (data.isNotEmpty()) {
                    _allCur.value = data
                    viewModelScope.launch {
                        repository.getFromCurrency().collect { fromCurrencyModel ->
                            fromCurrencyModel?.let {
                                updateFromCurrency(it)
                            } ?: run {
                                val currentLocale = Locale.getDefault()
                                val currency = Currency.getInstance(currentLocale)
                                val currencyCode = currency.currencyCode
                                val localeCurrency = getCurrencyModel(currencyCode)
                                localeCurrency?.let {
                                    updateFromCurrency(it)
                                } ?: run {
                                    updateFromCurrency(allCurrencies.value[0])
                                }
                            }
                            viewModelScope.launch {
                                repository.getToCurrency().collect { toCurrencyModel ->
                                    toCurrencyModel?.let {
                                        updateToCurrency(toCurrencyModel)
                                    } ?: run {
                                        // Set to either first or second conversion currency.
                                        val toCurr = when(allCurrencies.value[0] != fromCurrency.value) {
                                            true -> allCurrencies.value[0]
                                            false -> allCurrencies.value[1]
                                        }
                                        updateToCurrency(toCurr)
                                    }
                                    _dataloaded.value = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateFromCurrency(currencyModel: CurrencyModel) {
        if (fromCurrency.value == currencyModel) {
            return
        }
        Timber.d("Updating from currency to ${currencyModel.name}")

        _fromCurrency.update { currencyModel }
        fromCurrencyAmount.value = 1.0
        viewModelScope.launch {
            repository.getConversions(currencyModel).collect { conversionsList ->
                _conversions.value = conversionsList
                toCurrency.value?.let {
                    resetToCurrencyAmount()
                }
            }
        }
        repository.setFromCurrency(currencyModel)
    }

    fun updateToCurrency(currencyModel: CurrencyModel) {
        if (toCurrency.value == currencyModel) {
            return
        }
        Timber.d("Updating to currency to ${currencyModel.name}")

        _toCurrency.update { currencyModel }
        resetToCurrencyAmount()
        repository.setToCurrency(currencyModel)
    }

    fun updateFromCurrencyAmount(newValue: Double) {
        if (fromCurrencyAmount.value == newValue) {
            return
        }
        fromCurrencyAmount.update { newValue }
        toCurrencyAmount.update { newValue * curConverterValue }
    }

    fun updateToCurrencyAmount(newValue: Double) {
        if (toCurrencyAmount.value == newValue) {
            return
        }
        toCurrencyAmount.update { newValue }
        fromCurrencyAmount.update { newValue * (1 / curConverterValue) }
    }

    private fun getCurrencyModel(currencyCode: String): CurrencyModel? {
        allCurrencies.value.forEach { currencyModel ->
            if (currencyModel.abb == currencyCode) {
                return currencyModel
            }
        }
        return null
    }

    private fun resetToCurrencyAmount() {
        // TODO: Improve search logic to be better than linear
        conversions.value.forEach { conversionPair ->
            if (conversionPair.toCurrency == toCurrency.value) {
                toCurrencyAmount.value = fromCurrencyAmount.value * conversionPair.rate
                curConverterValue = conversionPair.rate
                return@forEach
            }
        }
    }
}