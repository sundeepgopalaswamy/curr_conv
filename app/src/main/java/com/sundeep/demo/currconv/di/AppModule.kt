package com.sundeep.demo.currconv.di

import android.content.Context
import androidx.room.Room
import com.sundeep.demo.currconv.data.CurrencyRepository
import com.sundeep.demo.currconv.data.DefaultCurrencyRepository
import com.sundeep.demo.currconv.data.datasource.LocalPreferencesDataSource
import com.sundeep.demo.currconv.data.datasource.PreferencesDataSource
import com.sundeep.demo.currconv.data.datasource.RemoteDataSource
import com.sundeep.demo.currconv.data.datasource.SimulatedRemoteDataSource
import com.sundeep.demo.currconv.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideCurrencyRepository(
        @ApplicationContext context: Context,
        database: AppDatabase,
        remoteDataSource: RemoteDataSource,
        preferencesDataSource: PreferencesDataSource
    ): CurrencyRepository {
        return DefaultCurrencyRepository(
            context,
            database,
            remoteDataSource,
            preferencesDataSource
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "currency_database.sqlite"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource {
        return SimulatedRemoteDataSource()
    }

    @Provides
    @Singleton
    fun providePreferencesDataSource(@ApplicationContext context: Context): PreferencesDataSource {
        return LocalPreferencesDataSource(context)
    }
}