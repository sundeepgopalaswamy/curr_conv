package com.sundeep.demo.currconv.data.datasource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

class LocalPreferencesDataSource(@ApplicationContext context: Context) : PreferencesDataSource {
    companion object {
        const val DEFAULT_CURRENCY = "default_currency"
    }

    private val sharedPreferences =
        context.getSharedPreferences("currency_converter_prefs", Context.MODE_PRIVATE)

    override fun getDefaultCurrency(): String? {
        return sharedPreferences.getString(DEFAULT_CURRENCY, null)
    }

    override fun setDefaultCurrency(currency: String) {
        sharedPreferences.edit().putString(DEFAULT_CURRENCY, currency).apply()
    }
}