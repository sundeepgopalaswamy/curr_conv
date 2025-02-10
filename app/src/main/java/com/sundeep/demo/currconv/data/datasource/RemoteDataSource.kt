package com.sundeep.demo.currconv.data.datasource

interface RemoteDataSource {
    suspend fun getCurrencies(): List<List<String>>
    suspend fun getConversions(): List<List<String>>
}