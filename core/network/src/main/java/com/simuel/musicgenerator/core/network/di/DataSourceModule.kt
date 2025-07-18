package com.simuel.musicgenerator.core.network.di

import com.simuel.musicgenerator.core.network.datasource.LoudlyRemoteDataSourceImpl
import com.simuel.musicgenerator.data.datasource.LoudlyRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    
    @Binds
    internal abstract fun bindLoudlyRemoteDataSource(
        dataSourceImpl: LoudlyRemoteDataSourceImpl
    ): LoudlyRemoteDataSource
}