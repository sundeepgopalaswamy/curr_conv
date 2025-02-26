package com.sundeep.demo.currconv.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sundeep.demo.currconv.data.datasource.RemoteDataSource
import com.sundeep.demo.currconv.room.AppDatabase
import com.sundeep.demo.currconv.util.fetchConversionsFromNetworkToDB
import com.sundeep.demo.currconv.util.fetchCurrenciesFromNetworkToDB
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class OfflineSyncWorker @AssistedInject constructor(
    private val dataSource: RemoteDataSource,
    private val database: AppDatabase,
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        Timber.i("Started")
        fetchCurrenciesFromNetworkToDB(dataSource, database)
        fetchConversionsFromNetworkToDB(dataSource, database)
        Timber.i("Finished")
        return Result.success()
    }
}