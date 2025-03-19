package com.sundeep.demo.currconv.data.datasource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

class LocalPreferencesDataSource(@ApplicationContext context: Context) : PreferencesDataSource {
    companion object {
        const val FROM_CURRENCY = "from_currency"
        const val TO_CURRENCY = "to_currency"
    }

    private val sharedPreferences =
        context.getSharedPreferences("currency_converter_prefs", Context.MODE_PRIVATE)

    override fun getFromCurrency(): String? {
        return sharedPreferences.getString(FROM_CURRENCY, null)
    }

    override fun setFromCurrency(currency: String) {
        sharedPreferences.edit().putString(FROM_CURRENCY, currency).apply()
    }

    override fun getToCurrency(): String? {
        return sharedPreferences.getString(TO_CURRENCY, null)
    }

    override fun setToCurrency(currency: String) {
        sharedPreferences.edit().putString(TO_CURRENCY, currency).apply()
    }
}