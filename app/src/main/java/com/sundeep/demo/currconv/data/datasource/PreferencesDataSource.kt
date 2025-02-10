package com.sundeep.demo.currconv.data.datasource

interface PreferencesDataSource {
    fun getDefaultCurrency(): String?
    fun setDefaultCurrency(currency: String)
}