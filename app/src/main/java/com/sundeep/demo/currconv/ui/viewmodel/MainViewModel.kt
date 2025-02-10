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
    private var _curCurrency = MutableStateFlow<CurrencyModel?>(null)
    private val _dataloaded = MutableStateFlow(false)
    val allCurrencies: StateFlow<List<CurrencyModel>> get() = _allCur
    val conversions: StateFlow<List<ConversionPairModel>> get() = _conversions
    val curCurrency: StateFlow<CurrencyModel?> get() = _curCurrency
    val dataloaded: StateFlow<Boolean> get() = _dataloaded

    init {
        viewModelScope.launch {
            repository.getAllCurrencies().collect { data ->
                Timber.i("Populated with ${data.size} currencies")
                if (data.isNotEmpty()) {
                    _allCur.value = data
                    viewModelScope.launch {
                        repository.getDefaultCurrency().collect { defaultCurrency ->
                            defaultCurrency?.let {
                                updateCurrency(it)
                            } ?: run {
                                val currentLocale = Locale.getDefault()
                                val currency = Currency.getInstance(currentLocale)
                                val currencyCode = currency.currencyCode
                                val localeCurrency = getCurrencyModel(currencyCode)
                                localeCurrency?.let {
                                    updateCurrency(it)
                                } ?: run {
                                    updateCurrency(allCurrencies.value[0])
                                }
                            }
                            _dataloaded.value = true
                        }
                    }
                }
            }
        }
    }

    fun updateCurrency(currencyModel: CurrencyModel) {
        Timber.d("Updating Currency to ${currencyModel.name}")

        _curCurrency.update { currencyModel }
        viewModelScope.launch {
            repository.getConversions(currencyModel).collect {
                _conversions.value = it
            }
        }
        repository.setDefaultCurrency(currencyModel)
    }

    private fun getCurrencyModel(currencyCode: String): CurrencyModel? {
        allCurrencies.value.forEach { currencyModel ->
            if (currencyModel.abb == currencyCode) {
                return currencyModel
            }
        }
        return null
    }
}