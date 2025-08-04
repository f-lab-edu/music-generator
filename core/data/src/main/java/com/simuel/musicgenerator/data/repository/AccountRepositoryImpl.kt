package com.simuel.musicgenerator.data.repository

import com.simuel.musicgenerator.data.datasource.LoudlyRemoteDataSource
import com.simuel.musicgenerator.domain.model.AccountLimitInfo
import com.simuel.musicgenerator.domain.repository.AccountRepository
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: LoudlyRemoteDataSource
) : AccountRepository {

    override suspend fun getAccountLimits(): List<AccountLimitInfo> {
        val limitDtos = remoteDataSource.getAccountLimits()
        
        return limitDtos.map { limitDto ->
            AccountLimitInfo(
                requestType = limitDto.requestType,
                limit = limitDto.limit,
                used = limitDto.used,
                left = limitDto.left,
                dateFrom = limitDto.dateFrom,
                dateTo = limitDto.dateTo
            )
        }
    }
}