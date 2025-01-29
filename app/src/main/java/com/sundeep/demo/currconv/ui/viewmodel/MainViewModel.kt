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
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(@get:VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) var repository: CurrencyRepository) :
    ViewModel() {
    private val _allCur = MutableStateFlow<List<CurrencyModel>>(emptyList())
    private val _conversions = MutableStateFlow<List<ConversionPairModel>>(emptyList())
    private var _curCurrencyIndex = MutableStateFlow(-1)
    private val _dataloaded = MutableStateFlow(false)
    val allCurrencies: StateFlow<List<CurrencyModel>> get() = _allCur
    val conversions: StateFlow<List<ConversionPairModel>> get() = _conversions
    val curCurrencyIndex: StateFlow<Int> get() = _curCurrencyIndex
    val dataloaded: StateFlow<Boolean> get() = _dataloaded

    init {
        viewModelScope.launch {
            repository.getAllCurrencies().collect { data ->
                Timber.i("Populated with ${data.size} currencies")
                if (data.isNotEmpty()) {
                    _allCur.value = data
                    _dataloaded.value = true
                }
            }
        }
    }

    fun updateCurrency(index: Int) {
        Timber.d("Updating Currency to currency with index $index")
        if (index >= 0 && index < allCurrencies.value.size) {
            _curCurrencyIndex.update { index }
            viewModelScope.launch {
                repository.getConversions(allCurrencies.value[curCurrencyIndex.value]).collect {
                    _conversions.value = it
                }
            }
        }
    }

    fun getCurrencyModelIndex(currencyCode: String): Int {
        allCurrencies.value.forEachIndexed { index, currencyModel ->
            if (currencyModel.abb == currencyCode) {
                return index
            }
        }
        return -1
    }
}