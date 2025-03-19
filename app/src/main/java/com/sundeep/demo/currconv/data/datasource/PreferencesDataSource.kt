package com.sundeep.demo.currconv.data.datasource

interface PreferencesDataSource {
    fun getFromCurrency(): String?
    fun setFromCurrency(currency: String)
    fun getToCurrency(): String?
    fun setToCurrency(currency: String)
}